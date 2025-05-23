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
    public GroupResponseDto createGroup(GroupCreateRequestDto requestDto, UserEntity user) {
        MyAlbum myAlbum = new MyAlbum();
        myAlbum.setMyalbumName(requestDto.getGroupName());
        myAlbum.setMyalbumDescription(requestDto.getGroupDescription());
        myAlbum.setUserId(user.getUserId());
        MyAlbum savedGroup = myAlbumRepository.save(myAlbum);

        AlbumMembers member = new AlbumMembers();
        member.setMyAlbum(savedGroup);
        member.setUserEntity(user);
        albumMembersRepository.save(member);

        return GroupResponseDto.fromEntity(savedGroup);
    }

    // 그룹 정보 + 구성원 반환
    @Transactional(readOnly = true)
    public GroupFullInfoResponseDto getGroupFullInfo(Long groupId) {
        // MyAlbum ID가 Integer이므로, Long 타입의 groupId를 Integer로 변환하여 사용합니다.
        Integer groupIdInt = groupId.intValue();

        // 수정: MyAlbum ID는 Integer이므로 groupIdInt를 사용합니다.
        MyAlbum group = myAlbumRepository.findById(groupIdInt) // 이제 MyAlbumRepository는 Integer를 기대합니다.
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
        // Long 타입의 groupId를 Integer로 변환합니다. MyAlbum ID는 Integer이기 때문입니다.
        Integer groupIdInt = groupId.intValue();

        // 수정: MyAlbumRepository는 Integer ID를 기대하므로 groupIdInt를 사용합니다.
        MyAlbum group = myAlbumRepository.findById(groupIdInt) // 이제 groupIdInt (Integer)를 사용합니다.
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

        Album album = albumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByAlbum_AlbumId(albumIdInt, PageRequest.of(page, size))
                .getContent();

        return AlbumWithPostsResponseDto.from(album, posts);
    }

    // 게시물 생성
    @Transactional
    public PostResponseDto createPost(Long albumId, PostCreateRequestDto requestDto, MultipartFile photoFile, UserEntity user) throws IOException {
        Integer albumIdInt = albumId.intValue();

        Album album = albumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        Post post = new Post();
        post.setAlbum(album);
        post.setUser(user);
        post.setPostText(requestDto.getPostContent());
        post.setPostDescription(requestDto.getPostDescription() != null ? requestDto.getPostDescription() : "");

        post.setLocation(requestDto.getLocation());
        post.setMakingTime(requestDto.getPostTime());
        // ------------------------------------

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

        Album album = albumRepository.findById(albumIdInt)
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


    // 친구 중에서 그룹에 없는 사람만 필터링하여 초대하기
    @Transactional(readOnly = true)
    public List<UserSummaryDto> getFriendsExcludingGroup(Long groupId, Long userId) {
        Integer groupIdInt = groupId.intValue();

        // Friend 엔티티 리스트에서 친구 상태 true인 친구 목록 조회
        List<Friend> friends = friendRepository.findByFromUserIdAndAreWeFriendTrue(userId);

        // Friend.toUserId 로 UserEntity 조회
        List<UserEntity> friendUsers = friends.stream()
                .map(friend -> userRepository.findById(friend.getToUserId()).orElse(null))
                .filter(user -> user != null)
                .collect(Collectors.toList());

        // 그룹 멤버 조회
        List<UserEntity> groupMembers = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                .stream()
                .map(AlbumMembers::getUserEntity)
                .collect(Collectors.toList());

        // 그룹에 없는 친구만 필터링해서 DTO 변환 후 반환
        return friendUsers.stream()
                .filter(friend -> !groupMembers.contains(friend))
                .map(UserSummaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 그룹에 친구 초대
    @Transactional
    public void inviteToGroup(Long groupId, UserEntity inviter, List<Long> friendIds) {
        // Long 타입의 groupId를 Integer로 변환합니다. MyAlbum ID는 Integer이기 때문입니다.
        Integer groupIdInt = groupId.intValue();

        // 수정: MyAlbumRepository는 Integer ID를 기대하므로 groupIdInt를 사용합니다.
        MyAlbum group = myAlbumRepository.findById(groupIdInt) // groupId 대신 groupIdInt를 사용합니다.
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));

        List<Long> existingMemberUserIds = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                .stream()
                .map(am -> am.getUserEntity().getUserId())
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
    @Transactional
    public void removeMemberFromGroup(Long groupId, Long userIdToRemove) {
        Integer groupIdInt = groupId.intValue();

        MyAlbum group = myAlbumRepository.findById(groupIdInt) // groupId 대신 groupIdInt를 사용합니다.
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));

        AlbumMembers memberToRemove = albumMembersRepository.findByMyAlbum_MyalbumIdAndUserEntity_UserId(groupIdInt, userIdToRemove)
                .orElseThrow(() -> new EntityNotFoundException("그룹에서 해당 멤버를 찾을 수 없습니다."));

        // 멤버 삭제
        albumMembersRepository.delete(memberToRemove);
    }
}