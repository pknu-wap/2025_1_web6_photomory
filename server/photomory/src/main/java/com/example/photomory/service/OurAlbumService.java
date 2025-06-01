package com.example.photomory.service;

import com.example.photomory.domain.NotificationType;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OurAlbumService {

    private final OurAlbumRepository ourAlbumRepository;
    private final AlbumMembersRepository albumMembersRepository;
    private final FriendRepository friendRepository;
    private final OurPostRepository ourPostRepository;
    private final CommentRepository commentRepository;
    private final MyAlbumRepository myAlbumRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final S3UrlResponseService s3UrlResponseService;
    private final PhotoRepository photoRepository;
    private final NotificationService notificationService;
    private final UserGroupRepository userGroupRepository;

    // 그룹 생성 (앨범 자동 생성 없음)
    @Transactional
    public GroupResponseDto createGroup(GroupCreateRequestDto requestDto, UserEntity user) {

        UserGroup userGroup = UserGroup.builder()
                .groupName(requestDto.getGroupName())
                .groupDescription(requestDto.getGroupDescription())
                .isGroup(true)
                .build();

        UserGroup savedGroup = userGroupRepository.save(userGroup);

        AlbumMembers.AlbumMembersId memberId = new AlbumMembers.AlbumMembersId(
                user.getUserId(),
                savedGroup.getId()
        );
        AlbumMembers creatorMember = AlbumMembers.builder()
                .id(memberId)
                .userEntity(user)
                .userGroup(savedGroup)
                .build();
        albumMembersRepository.save(creatorMember);

        // 앨범 자동 생성 로직 없음

        return GroupResponseDto.fromEntity(savedGroup);
    }

    // 앨범 생성
    @Transactional
    public AlbumResponseDto createAlbum(Long groupId, AlbumCreateRequestDto requestDto, UserEntity user) {
        Integer groupIdInt = groupId.intValue();

        OurAlbum group = ourAlbumRepository.findById(groupIdInt)
                .orElseThrow(() -> new EntityNotFoundException("그룹(OurAlbum)을 찾을 수 없습니다."));

        OurAlbum newOurAlbum = new OurAlbum();
        newOurAlbum.setAlbumName(requestDto.getAlbumName());

        if (requestDto.getAlbumTags() != null && !requestDto.getAlbumTags().isEmpty()) {
            String tags = String.join(",", requestDto.getAlbumTags());
            newOurAlbum.setAlbumTag(tags);
        } else {
            newOurAlbum.setAlbumTag(null);
        }

        newOurAlbum.setAlbumMakingTime(requestDto.getAlbumMakingTime());
        newOurAlbum.setAlbumDescription(requestDto.getAlbumDescription());
        newOurAlbum.setUser(user);

        OurAlbum savedAlbum = ourAlbumRepository.save(newOurAlbum);

        return AlbumResponseDto.fromEntity(savedAlbum);
    }

    // 앨범 상세정보 + 포스트 목록
    @Transactional(readOnly = true)
    public AlbumWithPostsResponseDto getAlbumWithPosts(Long albumId, int page, int size) {
        Integer albumIdInt = albumId.intValue();

        OurAlbum ourAlbum = ourAlbumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        List<OurPost> posts = ourPostRepository.findByOurAlbum_AlbumId(albumIdInt, PageRequest.of(page, size))
                .getContent();

        return AlbumWithPostsResponseDto.from(ourAlbum, posts);
    }

    // 게시물 생성
    @Transactional
    public PostResponseDto createPost(Long albumId, PostCreateRequestDto requestDto, MultipartFile photoFile, Long userId) throws IOException {
        Integer albumIdInt = albumId.intValue();

        OurAlbum ourAlbum = ourAlbumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        OurPost ourPost = new OurPost();
        ourPost.setOurAlbum(ourAlbum);
        ourPost.setUser(user);
        ourPost.setPostText(requestDto.getPostTitle());
        ourPost.setMakingTime(requestDto.getPostTime() != null ? requestDto.getPostTime() : LocalDateTime.now());

        OurPost savedPost = ourPostRepository.save(ourPost);

        if (photoFile != null && !photoFile.isEmpty()) {
            String uploadedUrl = s3Service.uploadFile(photoFile);

            Photo photo = new Photo();
            photo.setOurPost(savedPost);
            photo.setPostId(savedPost.getPostId());
            photo.setPostType("OUR");
            photo.setPhotoUrl(uploadedUrl);
            photo.setPhotoName(requestDto.getPhotoName());
            photo.setPhotoMakingTime(requestDto.getPhotoMakingTime());
            photo.setUser(user);

            photoRepository.save(photo);
        }

        return PostResponseDto.fromEntity(savedPost);
    }

    // 게시글 삭제
    @Transactional
    public void deletePostWithFile(Integer albumId, Integer postId, UserEntity currentUser) {
        OurAlbum ourAlbum = ourAlbumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("앨범을 찾을 수 없습니다. id=" + albumId));

        OurPost ourPost = ourPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        if (!ourPost.getOurAlbum().equals(ourAlbum)) {
            throw new IllegalArgumentException("게시글이 해당 앨범에 속하지 않습니다.");
        }

        if (ourPost.getPhotos() != null) {
            for (Photo photo : ourPost.getPhotos()) {
                if (photo.getPhotoUrl() != null && !photo.getPhotoUrl().isEmpty()) {
                    s3Service.deleteFile(photo.getPhotoUrl());
                }
            }
        }

        ourPostRepository.delete(ourPost);
    }

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Integer albumId, Integer postId, UserEntity user, String commentsText) {
        Comment comment = new Comment();
        OurPost post = null;

        // 앨범과 게시글 중 하나만 연결
        if (postId != null) {
            post = ourPostRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
            comment.setOurPost(post);
            comment.setOurAlbum(null);
            comment.setEveryAlbum(null);
            comment.setEveryPost(null);
        } else if (albumId != null) {
            OurAlbum album = ourAlbumRepository.findById(albumId)
                    .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));
            comment.setOurAlbum(album);
            comment.setOurPost(null);
            comment.setEveryAlbum(null);
            comment.setEveryPost(null);
        } else {
            throw new IllegalArgumentException("게시글 ID 또는 앨범 ID 중 하나는 반드시 입력되어야 합니다.");
        }

        comment.setUser(user);
        comment.setCommentText(commentsText);

        comment.validateParentRelationship();

        Comment savedComment = commentRepository.save(comment);

        // 알림 전송 (postText만 이용, 변수명도 postText로 통일)
        if (post != null) {
            UserEntity postWriter = post.getUser();

            // 본인이 자기 글에 단 댓글은 알림 전송 X
            if (!user.getUserId().equals(postWriter.getUserId())) {
                String postText = post.getPostText() != null && !post.getPostText().isEmpty()
                        ? post.getPostText()
                        : "제목 없음";

                String message = user.getUserName() + "님이 '" + postText + "' 게시글에 댓글을 달았습니다.";

                notificationService.sendNotification(
                        postWriter.getUserId(),
                        user.getUserId(),
                        message,
                        NotificationType.COMMENT,
                        post.getPostId().longValue()
                );
            }
        }

        return CommentResponseDto.fromEntity(savedComment);
    }

    // 친구 중에서 그룹에 없는 사람만 필터링하여 초대하기
    @Transactional(readOnly = true)
    public List<UserSummaryDto> getInvitableFriends(Long groupId, Long userId) {
        Integer groupIdInt = groupId.intValue();

        List<Friend> friends = friendRepository.findByFromUserIdAndAreWeFriendTrue(userId);

        List<UserEntity> friendUsers = friends.stream()
                .map(friend -> userRepository.findById(friend.getToUserId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<AlbumMembers> albumMembers = albumMembersRepository.findByUserGroup_Id(groupId);

        List<UserEntity> groupMembers = albumMembers.stream()
                .map(AlbumMembers::getUserEntity)
                .collect(Collectors.toList());

        return friendUsers.stream()
                .filter(friend -> !groupMembers.contains(friend))
                .map(UserSummaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDto> getFriendsExcludingGroup(Long groupId, Long userId) {
        Integer groupIdInt = groupId.intValue();

        List<Friend> friends = friendRepository.findByFromUserIdAndAreWeFriendTrue(userId);

        List<UserEntity> friendUsers = friends.stream()
                .map(friend -> userRepository.findById(friend.getToUserId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        List<AlbumMembers> albumMembers = albumMembersRepository.findByUserGroup_Id(groupId);

        List<UserEntity> groupMembers = albumMembers.stream()
                .map(AlbumMembers::getUserEntity)
                .toList();

        return friendUsers.stream()
                .filter(friend -> !groupMembers.contains(friend))
                .map(UserSummaryDto::fromEntity)
                .toList();
    }

    // 그룹에 친구 초대
    @Transactional
    public void inviteToGroup(Long groupId, UserEntity inviter, List<Long> friendIds) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("그룹(앨범)을 찾을 수 없습니다."));

        List<Long> existingMemberUserIds = albumMembersRepository.findByUserGroup_Id(groupId)
                .stream()
                .map(am -> am.getUserEntity().getUserId())
                .collect(Collectors.toList());

        for (Long friendId : friendIds) {
            if (!existingMemberUserIds.contains(friendId)) {
                UserEntity friend = userRepository.findById(friendId)
                        .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

                AlbumMembers member = AlbumMembers.builder()
                        .userEntity(friend)
                        .userGroup(group)
                        .build();

                albumMembersRepository.save(member);

                String message = inviter.getUserName() + "님의 " + group.getGroupName() + " 그룹에 초대되었습니다.";

                notificationService.sendNotification(
                        friend.getUserId(),
                        inviter.getUserId(),
                        message,
                        NotificationType.GROUP_INVITE,
                        groupId
                );
            }
        }
    }

    // 강퇴
    @Transactional
    public void removeMemberFromGroup(Long groupId, Long userIdToRemove, UserEntity currentUser) {
        Integer groupIdInt = groupId.intValue();

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("그룹(앨범)을 찾을 수 없습니다."));

        AlbumMembers memberToRemove = albumMembersRepository.findByUserGroup_IdAndUserEntity_UserId(groupId, userIdToRemove)
                .orElseThrow(() -> new EntityNotFoundException("그룹에서 해당 멤버를 찾을 수 없습니다."));

        albumMembersRepository.delete(memberToRemove);

        // 알림 관련 코드는 주석 처리해두었습니다.
    }

    // 앨범 삭제
    @Transactional
    public void deleteAlbum(Long albumId, UserEntity currentUser) {
        Integer albumIdInt = albumId.intValue();

        OurAlbum ourAlbum = ourAlbumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("해당 앨범을 찾을 수 없습니다."));

        if (!ourAlbum.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("앨범 삭제 권한이 없습니다. (앨범을 생성한 사용자만 삭제 가능)");
        }

        List<OurPost> postsToDelete = ourPostRepository.findByOurAlbum_AlbumId(albumIdInt);

        for (OurPost ourPost : postsToDelete) {
            commentRepository.deleteByOurPost_PostId(ourPost.getPostId());

            if (ourPost.getPhotos() != null && !ourPost.getPhotos().isEmpty()) {
                for (Photo photo : ourPost.getPhotos()) {
                    if (photo.getPhotoUrl() != null && !photo.getPhotoUrl().isEmpty()) {
                        s3Service.deleteFile(photo.getPhotoUrl());
                    }
                }
            }
            ourPostRepository.delete(ourPost);
        }
        ourAlbumRepository.delete(ourAlbum);
    }

    // 그룹정보 보기
    @Transactional(readOnly = true)
    public OurAlbumResponseDefaultDto getGroupFullInfo(Long groupId) {
        // groupId에 해당하는 AlbumMembers 조회 (멤버 확인)
        List<AlbumMembers> members = albumMembersRepository.findByUserGroup_Id(groupId);

        if (members.isEmpty()) {
            throw new EntityNotFoundException("해당 그룹을 찾을 수 없습니다. groupId=" + groupId);
        }

        List<OurAlbumResponseDefaultDto.Member> membersDto = members.stream()
                .map(member -> OurAlbumResponseDefaultDto.Member.builder()
                        .userId(member.getUserEntity().getUserId())
                        .userName(member.getUserEntity().getUserName())
                        .userPhotourl(member.getUserEntity().getUserPhotourl())
                        .build())
                .collect(Collectors.toList());

        UserGroup userGroup = members.get(0).getUserGroup();

        List<OurAlbum> albums = ourAlbumRepository.findAllByGroupIdWithPostsAndPhotos(groupId);

        List<OurAlbumResponseDefaultDto.Album> albumsDto = albums.stream()
                .map(this::toAlbumDto)
                .collect(Collectors.toList());

        return OurAlbumResponseDefaultDto.builder()
                .groupId(groupId)
                .groupName(userGroup.getGroupName())
                .members(membersDto)
                .albums(albumsDto)
                .build();
    }

    // 우리의 추억 페이지 기본 데이터 반환
    @Transactional(readOnly = true)
    public List<OurAlbumResponseDefaultDto> getAllGroupsDetailForUser(Long userId) {
        List<AlbumMembers> groupMemberships = albumMembersRepository.findGroupMembershipsByUserId(userId);

        if (groupMemberships.isEmpty()) {
            return new ArrayList<>();
        }

        return groupMemberships.stream()
                .map(this::toGroupDetailDto)
                .collect(Collectors.toList());
    }

    private OurAlbumResponseDefaultDto toGroupDetailDto(AlbumMembers membership) {
        UserGroup group = membership.getUserGroup();
        Long groupId = group.getId();

        List<OurAlbumResponseDefaultDto.Member> membersDto = albumMembersRepository.findByUserGroup_Id(groupId)
                .stream()
                .map(member -> OurAlbumResponseDefaultDto.Member.builder()
                        .userId(member.getUserEntity().getUserId())
                        .userName(member.getUserEntity().getUserName())
                        .userPhotourl(member.getUserEntity().getUserPhotourl())
                        .build())
                .collect(Collectors.toList());

        List<OurAlbum> ourAlbums = ourAlbumRepository.findAllByGroupIdWithPostsAndPhotos(groupId);

        List<OurAlbumResponseDefaultDto.Album> albumsDto = ourAlbums.stream()
                .map(this::toAlbumDto)
                .collect(Collectors.toList());

        return OurAlbumResponseDefaultDto.builder()
                .groupId(groupId)
                .groupName(group.getGroupName())
                .members(membersDto)
                .albums(albumsDto)
                .build();
    }

    private OurAlbumResponseDefaultDto.Album toAlbumDto(OurAlbum album) {
        Set<OurPost> posts = album.getPosts();

        List<OurAlbumResponseDefaultDto.Post> postsDto = posts.stream()
                .map(post -> {
                    List<OurAlbumResponseDefaultDto.Photo> photosDto = post.getPhotos()
                            .stream()
                            .map(photo -> OurAlbumResponseDefaultDto.Photo.builder()
                                    .photoId(photo.getPhotoId())
                                    .photoName(photo.getPhotoName())
                                    .photoUrl(photo.getPhotoUrl())
                                    .build())
                            .collect(Collectors.toList());

                    return OurAlbumResponseDefaultDto.Post.builder()
                            .postId(post.getPostId())
                            .postContent(post.getPostText())
                            .postMakingTime(post.getMakingTime().toString())
                            .build();
                })
                .collect(Collectors.toList());

        return OurAlbumResponseDefaultDto.Album.builder()
                .albumId(album.getAlbumId())
                .albumName(album.getAlbumName())
                .albumDescription(album.getAlbumDescription())
                .albumTag(album.getAlbumTag()) // ← 필요 시 추가
                .albumMakingtime(album.getAlbumMakingTime().toString()) // 필드명에 맞게 수정
                .posts(postsDto)
                .build();
    }
}