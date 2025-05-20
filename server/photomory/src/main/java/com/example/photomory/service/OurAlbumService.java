package com.example.photomory.service;

import com.example.photomory.dto.*;
import com.example.photomory.entity.*;
import com.example.photomory.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OurAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMembersRepository albumMembersRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MyAlbumRepository myAlbumRepository;

    public OurAlbumService(
            AlbumRepository albumRepository,
            AlbumMembersRepository albumMembersRepository,
            FriendRepository friendRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            MyAlbumRepository myAlbumRepository) {
        this.albumRepository = albumRepository;
        this.albumMembersRepository = albumMembersRepository;
        this.friendRepository = friendRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.myAlbumRepository = myAlbumRepository;
    }

    // ✅ 1. 그룹 생성
    @Transactional
    public MyAlbumResponseDto createGroup(MyAlbumCreateRequestDto requestDto, UserEntity user) {
        MyAlbum myAlbum = new MyAlbum();
        myAlbum.setMyalbumName(requestDto.getGroupName());               // 엔티티 필드명에 맞춤
        myAlbum.setMyalbumDescription(requestDto.getGroupDescription());  // 엔티티 필드명에 맞춤
        myAlbum.setUserId(user.getUserId());                              // userId 직접 세팅 (MyAlbum에 UserEntity 관계 없으면)

        MyAlbum savedGroup = myAlbumRepository.save(myAlbum);

        // 그룹 생성 시 생성자도 멤버로 등록
        AlbumMembers member = new AlbumMembers();
        member.setMyAlbum(savedGroup);
        member.setUserEntity(user);
        albumMembersRepository.save(member);

        return MyAlbumResponseDto.fromEntity(savedGroup);
    }

    // ✅ 2. 앨범 생성
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

    // ✅ 3. 게시물 생성
    @Transactional
    public PostResponseDto createPost(Integer albumId, PostCreateRequestDto requestDto, UserEntity user) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        Post post = new Post();
        post.setAlbum(album);
        post.setUser(user);

        // 엔티티 필드명에 맞춰 DTO 필드와 매핑
        post.setPostText(requestDto.getPostContent());       // postText 필드 사용
        post.setPostDescription(requestDto.getPostDescription() != null ? requestDto.getPostDescription() : "");
        // 사진 URL, 시간 등 필드가 Post에 정의되어 있지 않다면 엔티티에 추가하거나 DTO 맞춤 필요

        // 예시로 likesCount 0으로 초기화
        post.setLikesCount(0);

        Post savedPost = postRepository.save(post);
        return PostResponseDto.fromEntity(savedPost);
    }

    // ✅ 4. 앨범 전체 조회
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

    // ✅ 5. 댓글 생성
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
