package com.example.photomory.service;

import com.example.photomory.dto.*;
import com.example.photomory.entity.*;
import com.example.photomory.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.example.photomory.dto.PostZoomDetailResponseDto;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OurAlbumService {

    private final AlbumRepository albumRepository;               // Album ID: Integer
    private final AlbumMembersRepository albumMembersRepository; // MyAlbum ID: Integer
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;                 // Post ID: Integer
    private final CommentRepository commentRepository;
    private final MyAlbumRepository myAlbumRepository;           // MyAlbum ID: Integer
    private final S3Service s3Service;
    private final UserRepository userRepository;                 // User ID: Long

    // 그룹 생성
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

    // 그룹 정보 + 구성원 반환
    @Transactional(readOnly = true)
    public GroupFullInfoResponseDto getGroupFullInfo(Long groupId) {
        Integer groupIdInt = groupId.intValue();

        MyAlbum group = myAlbumRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));

        List<UserSummaryDto> members = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                .stream()
                .map(member -> UserSummaryDto.fromEntity(member.getUserEntity()))
                .collect(Collectors.toList());

        return GroupFullInfoResponseDto.from(group, members);
    }

    // 앨범 생성
    @Transactional
    public AlbumResponseDto createAlbum(Long groupId, AlbumCreateRequestDto requestDto, UserEntity user) {
        Integer groupIdInt = groupId.intValue();

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

    // 앨범 상세정보 + 포스트 목록 (페이징 적용)
    @Transactional(readOnly = true)
    public AlbumWithPostsResponseDto getAlbumWithPosts(Long albumId, int page, int size) {
        Integer albumIdInt = albumId.intValue();

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByAlbum_AlbumId(albumIdInt, PageRequest.of(page, size))
                .getContent();

        return AlbumWithPostsResponseDto.from(album, posts);
    }

    // 게시물 생성
    @Transactional
    public PostResponseDto createPost(Long albumId, PostCreateRequestDto requestDto, MultipartFile photoFile, UserEntity user) throws IOException {
        Integer albumIdInt = albumId.intValue();

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        Post post = new Post();
        post.setAlbum(album);
        post.setUser(user);
        post.setPostText(requestDto.getPostContent());
        post.setPostDescription(requestDto.getPostDescription() != null ? requestDto.getPostDescription() : "");
        post.setLikesCount(0);

        if (photoFile != null && !photoFile.isEmpty()) {
            String photoUrl = s3Service.uploadFile(photoFile);
            post.setPhotoUrl(photoUrl);
        }

        Post savedPost = postRepository.save(post);
        return PostResponseDto.fromEntity(savedPost);
    }

    // 게시물 클릭 시 상세 보기 (사진 확대, 댓글, 좋아요 수)
    @Transactional(readOnly = true)
    public PostZoomDetailResponseDto getPostZoomDetail(Long postId) {
        Integer postIdInt = postId.intValue();

        Post post = postRepository.findById(postIdInt)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        List<Comment> comments = commentRepository.findByPost_PostId(postId);
        return PostZoomDetailResponseDto.from(post, comments);
    }

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long albumId, Long postId, UserEntity user, String text) {
        Integer albumIdInt = albumId.intValue();
        Integer postIdInt = postId.intValue();

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));
        Post post = postRepository.findById(postIdInt)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setAlbum(album);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCommentsText(text);

        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.fromEntity(saved);
    }


    @Transactional(readOnly = true)
    public List<CalendarTagResponseDto> getCalendarTags(Long groupId) {
        myAlbumRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByAlbum_MyAlbum_MyalbumId(groupId);

        return posts.stream()
                .map(post -> new CalendarTagResponseDto(
                        post.getPostMakingTime(),  // 필드 이름은 Post entity에 맞게 수정하세요
                        post.getPhotoUrl(),
                        post.getPostDescription()
                ))
                .collect(Collectors.toList());
    }


    // 친구 중에서 그룹에 없는 사람만 필터링하여 초대하기
    @Transactional(readOnly = true)
    public List<UserSummaryDto> getFriendsExcludingGroup(Long groupId, Long userId) {
        Integer groupIdInt = groupId.intValue();

        List<UserEntity> friends = friendRepository.findFriendsByUserId(userId);
        List<UserEntity> groupMembers = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                .stream()
                .map(AlbumMembers::getUserEntity)
                .collect(Collectors.toList());

        return friends.stream()
                .filter(friend -> !groupMembers.contains(friend))
                .map(UserSummaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 그룹에 친구 초대
    @Transactional
    public void inviteToGroup(Long groupId, UserEntity inviter, List<Long> friendIds) {
        Integer groupIdInt = groupId.intValue();
        Long groupIdLong = groupIdInt.longValue();

        MyAlbum group = myAlbumRepository.findById(groupIdLong)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));


        // userId는 Long 타입 유지
        List<Long> existingMemberUserIds = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                .stream()
                .map(am -> am.getUserEntity().getUserId()) // Long 유지
                .collect(Collectors.toList());

        for (Long friendId : friendIds) {
            if (!existingMemberUserIds.contains(friendId)) {
                UserEntity friend = userRepository.findById(friendId)
                        .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
                AlbumMembers member = new AlbumMembers();
                member.setMyAlbum(group);
                member.setUserEntity(friend);
                albumMembersRepository.save(member);
            }
        }
    }
}
