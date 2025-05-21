package com.example.photomory.service;

import com.example.photomory.dto.*;
import com.example.photomory.entity.*;
import com.example.photomory.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OurAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMembersRepository albumMembersRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MyAlbumRepository myAlbumRepository;
    private final S3Service s3Service;  // S3Service 주입

    // 1. 그룹 생성
    @Transactional
    public MyAlbumResponseDto createGroup(GroupCreateRequestDto requestDto, UserEntity user) {
        MyAlbum myAlbum = new MyAlbum();
        myAlbum.setMyalbumName(requestDto.getGroupName());
        myAlbum.setMyalbumDescription(requestDto.getGroupDescription());
        myAlbum.setUserId(user.getUserId());

        MyAlbum savedGroup = myAlbumRepository.save(myAlbum);

        AlbumMembers member = new AlbumMembers();
        member.setMyAlbum(savedGroup);
        member.setUserEntity(user);
        albumMembersRepository.save(member);

        return MyAlbumResponseDto.fromEntity(savedGroup);
    }

    // 2. 앨범 생성
    @Transactional
    public AlbumResponseDto createAlbum(Long groupId, AlbumCreateRequestDto requestDto, UserEntity user) {
        MyAlbum group = myAlbumRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));

        Album album = new Album();
        album.setAlbumName(requestDto.getAlbumName());
        album.setAlbumTag(requestDto.getAlbumTag());
        album.setAlbumMakingTime(requestDto.getAlbumMakingTime());
        album.setAlbumDescription(requestDto.getAlbumDescription());
        album.setMyAlbum(group);

        Album savedAlbum = albumRepository.save(album);
        return AlbumResponseDto.fromEntity(savedAlbum);
    }

    // 3. 게시물 생성 (사진 업로드 포함)
    @Transactional
    public PostResponseDto createPost(Integer albumId, PostCreateRequestDto requestDto, MultipartFile photoFile, UserEntity user) throws IOException {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        Post post = new Post();
        post.setAlbum(album);
        post.setUser(user);
        post.setPostText(requestDto.getPostContent());
        post.setPostDescription(requestDto.getPostDescription() != null ? requestDto.getPostDescription() : "");
        post.setLikesCount(0);

        // 사진 파일이 있으면 S3에 업로드 후 URL 저장
        if (photoFile != null && !photoFile.isEmpty()) {
            String photoUrl = s3Service.uploadFile(photoFile);
            post.setPhotoUrl(photoUrl); // Post 엔티티에 photoUrl 필드가 있어야 합니다.
        }

        Post savedPost = postRepository.save(post);
        return PostResponseDto.fromEntity(savedPost);
    }

    // 4. 앨범 전체 조회
    @Transactional(readOnly = true)
    public OurAlbumFullResponseDto getAlbumFullDetails(Integer albumId, Long requestUserId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        if (album.getPosts() == null || album.getPosts().isEmpty()) {
            throw new EntityNotFoundException("앨범에 게시물이 없습니다.");
        }

        Long albumOwnerUserId = album.getPosts().get(0).getUser().getUserId();
        boolean areFriends = friendRepository.existsByFromUserIdAndToUserId(requestUserId, albumOwnerUserId);
        if (!areFriends) {
            throw new SecurityException("친구가 아니면 접근할 수 없습니다.");
        }

        boolean isGroupMember = albumMembersRepository.existsByUserEntityUserIdAndMyAlbumMyalbumId(
                requestUserId.intValue(), album.getMyAlbum().getMyalbumId());
        if (!isGroupMember) {
            throw new SecurityException("그룹 멤버만 접근할 수 있습니다.");
        }

        List<Post> posts = postRepository.findByAlbum_AlbumId(albumId);
        List<PostWithCommentsResponseDto> postDtos = new ArrayList<>();

        for (Post post : posts) {
            List<Comment> comments = commentRepository.findByAlbum_AlbumIdAndPost_PostId(albumId, post.getPostId());
            postDtos.add(PostWithCommentsResponseDto.fromEntity(post, comments));
        }

        return OurAlbumFullResponseDto.from(album, postDtos);
    }

    // 5. 댓글 생성
    @Transactional
    public CommentResponseDto createComment(Integer albumId, Integer postId, UserEntity user, String text) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setAlbum(album);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCommentsText(text);

        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.fromEntity(saved);
    }
}
