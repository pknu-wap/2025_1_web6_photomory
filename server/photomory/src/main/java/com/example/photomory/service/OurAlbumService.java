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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
// import org.springframework.transaction.annotation.Transactional; // 중복 선언 제거

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

    // 그룹 생성
    @Transactional
    public GroupResponseDto createGroup(GroupCreateRequestDto requestDto, UserEntity user) {
        OurAlbum ourAlbum = new OurAlbum();
        ourAlbum.setAlbumName(requestDto.getGroupName());
        ourAlbum.setAlbumDescription(requestDto.getGroupDescription());
        ourAlbum.setAlbumMakingTime(LocalDateTime.now());
        ourAlbum.setUser(user);

        OurAlbum savedGroup = ourAlbumRepository.save(ourAlbum);

        AlbumMembers member = new AlbumMembers();
        member.setOurAlbum(savedGroup);
        member.setUserEntity(user);
        albumMembersRepository.save(member);

        return GroupResponseDto.fromEntity(savedGroup);
    }

    // 그룹 정보 + 구성원 반환
    @Transactional(readOnly = true)
    public GroupFullInfoResponseDto getGroupFullInfo(Long groupId) {
        Integer groupIdInt = groupId.intValue();

        OurAlbum group = ourAlbumRepository.findById(groupIdInt)
                .orElseThrow(() -> new EntityNotFoundException("그룹(앨범)을 찾을 수 없습니다."));

        List<UserSummaryDto> members = albumMembersRepository.findByOurAlbum_AlbumId(groupIdInt)
                .stream()
                .map(member -> UserSummaryDto.fromEntity(member.getUserEntity())) // member.getUserEntity()는 AlbumMembers 엔티티의 Getter입니다.
                .collect(Collectors.toList());

        return GroupFullInfoResponseDto.from(group, members);
    }

    // 앨범 생성 (이전과 동일, OurAlbum 사용)
    @Transactional
    public AlbumResponseDto createAlbum(Long groupId, AlbumCreateRequestDto requestDto, UserEntity user) {
        Integer groupIdInt = groupId.intValue();

        OurAlbum group = ourAlbumRepository.findById(groupIdInt) // <--- 수정: MyAlbumRepository 대신 OurAlbumRepository 사용
                .orElseThrow(() -> new EntityNotFoundException("그룹(OurAlbum)을 찾을 수 없습니다."));

        OurAlbum newOurAlbum = new OurAlbum(); // 변수명 ourAlbum -> newOurAlbum (혼동 방지)
        newOurAlbum.setAlbumName(requestDto.getAlbumName());

        if (requestDto.getAlbumTags() != null && !requestDto.getAlbumTags().isEmpty()) {
            String tags = String.join(",", requestDto.getAlbumTags());
            newOurAlbum.setAlbumTag(tags);
        } else {
            newOurAlbum.setAlbumTag(null);
        }

        newOurAlbum.setAlbumMakingTime(requestDto.getAlbumMakingTime());
        newOurAlbum.setAlbumDescription(requestDto.getAlbumDescription());
        newOurAlbum.setUser(user); // <--- 추가: OurAlbum 엔티티의 user 필드 설정

        OurAlbum savedAlbum = ourAlbumRepository.save(newOurAlbum);

        return AlbumResponseDto.fromEntity(savedAlbum); // AlbumResponseDto.fromEntity가 OurAlbum을 받도록 수정되어 있어야 합니다.
    }

    // 앨범 상세정보 + 포스트 목록 (페이징 적용)
    @Transactional(readOnly = true)
    public AlbumWithPostsResponseDto getAlbumWithPosts(Long albumId, int page, int size) {
        Integer albumIdInt = albumId.intValue();

        OurAlbum ourAlbum = ourAlbumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        List<OurPost> posts = ourPostRepository.findByOurAlbum_AlbumId(albumIdInt, PageRequest.of(page, size)) // <--- 수정!
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
            photo.setPostId(savedPost.getPostId());  // 여기서 post_id 값을 꼭 넣어줘야 합니다.
            photo.setPostType("OUR");                 // post_type 필수값 세팅
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

        OurPost ourPost = ourPostRepository.findById(postId) // Post 대신 OurPost 사용
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        if (!ourPost.getOurAlbum().equals(ourAlbum)) {
            throw new IllegalArgumentException("게시글이 해당 앨범에 속하지 않습니다.");
        }

        if (ourPost.getPhotos() != null) { // OurPost 사용
            for (Photo photo : ourPost.getPhotos()) { // OurPost 사용
                if (photo.getPhotoUrl() != null && !photo.getPhotoUrl().isEmpty()) {
                    s3Service.deleteFile(photo.getPhotoUrl());
                }
            }
        }

        ourPostRepository.delete(ourPost); // postRepository 대신 ourPostRepository 사용
    }

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Integer albumId, Integer postId, UserEntity user, String commentsText) {
        Comment comment = new Comment();

        // 앨범과 게시글 중 하나만 연결
        if (postId != null) {
            OurPost post = ourPostRepository.findById(postId)
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

        return CommentResponseDto.fromEntity(savedComment);
    }


    // 친구 중에서 그룹에 없는 사람만 필터링하여 초대하기
    @Transactional(readOnly = true)
    public List<UserSummaryDto> getInvitableFriends(Long groupId, Long userId) {
        Integer groupIdInt = groupId.intValue(); // 그룹 ID를 Integer로 변환

        List<Friend> friends = friendRepository.findByFromUserIdAndAreWeFriendTrue(userId);

        List<UserEntity> friendUsers = friends.stream()
                .map(friend -> userRepository.findById(friend.getToUserId()).orElse(null))
                .filter(user -> user != null)
                .collect(Collectors.toList());

        List<AlbumMembers> albumMembers = albumMembersRepository.findByOurAlbum_AlbumId(groupIdInt);

        List<UserEntity> groupMembers = albumMembers.stream()
                .map(AlbumMembers::getUserEntity) // AlbumMembers 객체에서 UserEntity를 가져옵니다.
                .collect(Collectors.toList());

        return friendUsers.stream()
                .filter(friend -> !groupMembers.contains(friend))
                .map(UserSummaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDto> getFriendsExcludingGroup(Long groupId, Long userId) {
        Integer groupIdInt = groupId.intValue(); // 그룹 ID를 Integer로 변환

        List<Friend> friends = friendRepository.findByFromUserIdAndAreWeFriendTrue(userId);

        List<UserEntity> friendUsers = friends.stream()
                .map(friend -> userRepository.findById(friend.getToUserId()).orElse(null))
                .filter(user -> user != null)
                .toList();

        List<AlbumMembers> albumMembers = albumMembersRepository.findByOurAlbum_AlbumId(groupIdInt);

        List<UserEntity> groupMembers = albumMembers.stream()
                .map(AlbumMembers::getUserEntity)
                .toList();

        return friendUsers.stream()
                .filter(friend -> !groupMembers.contains(friend))
                .map(UserSummaryDto::fromEntity)
                .toList();
    }

    // 그룹에 친구 초대 (이전과 동일)
    @Transactional
    public void inviteToGroup(Long groupId, UserEntity inviter, List<Long> friendIds) {
        Integer groupIdInt = groupId.intValue();

        OurAlbum group = ourAlbumRepository.findById(groupIdInt) // 우리의 AlbumRepository 사용
                .orElseThrow(() -> new EntityNotFoundException("그룹(앨범)을 찾을 수 없습니다."));

        List<Long> existingMemberUserIds = albumMembersRepository.findByOurAlbum_AlbumId(groupIdInt) // OurAlbum의 ID 사용
                .stream()
                .map(am -> am.getUserEntity().getUserId())
                .collect(Collectors.toList());

        for (Long friendId : friendIds) {
            if (!existingMemberUserIds.contains(friendId)) {
                UserEntity friend = userRepository.findById(friendId)
                        .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
                AlbumMembers member = new AlbumMembers();
                // setMyAlbum 대신 setOurAlbum 사용
                member.setOurAlbum(group); // <--- 이 부분 수정!
                member.setUserEntity(friend);
                albumMembersRepository.save(member);

                String message = inviter.getUserName() + "님의 " + group.getAlbumName() + " 그룹에 초대되었습니다."; // getMyalbumName -> getAlbumName
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

        OurAlbum group = ourAlbumRepository.findById(groupIdInt) // <--- 이 부분 수정!
                .orElseThrow(() -> new EntityNotFoundException("그룹(앨범)을 찾을 수 없습니다."));

        // 2. findByMyAlbum_MyalbumIdAndUserEntity_UserId 대신 findByOurAlbum_AlbumIdAndUserEntity_UserId 사용
        AlbumMembers memberToRemove = albumMembersRepository.findByOurAlbum_AlbumIdAndUserEntity_UserId(groupIdInt, userIdToRemove) // <--- 이 부분 수정!
                .orElseThrow(() -> new EntityNotFoundException("그룹에서 해당 멤버를 찾을 수 없습니다."));

        // 3. 멤버 삭제
        albumMembersRepository.delete(memberToRemove);

        // String message = currentUser.getUserName() + "님이 " + group.getAlbumName() + " 그룹에서 강퇴되었습니다.";
        // notificationService.sendNotification(userIdToRemove, currentUser.getUserId(), message, NotificationType.GROUP_KICK, groupId);
    }

    // 앨범 삭제 (이전과 동일, OurAlbum 사용)
    @Transactional
    public void deleteAlbum(Long albumId, UserEntity currentUser) {
        Integer albumIdInt = albumId.intValue();

        OurAlbum ourAlbum = ourAlbumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("해당 앨범을 찾을 수 없습니다."));

        if (!ourAlbum.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("앨범 삭제 권한이 없습니다. (앨범을 생성한 사용자만 삭제 가능)");
        }

        List<OurPost> postsToDelete = ourPostRepository.findByOurAlbum_AlbumId(albumIdInt); // 이전에 수정 완료

        for (OurPost ourPost : postsToDelete) {
            commentRepository.deleteByOurPost_PostId(ourPost.getPostId()); // 이전에 수정 완료

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


    // 우리의 추억 페이지 기본 데이터 반환
    @Transactional(readOnly = true)
    public List<OurAlbumResponseDefaultDto> getAllGroupsDetailForUser(Long userId) {
        System.out.println("\n--- [시작] getAllGroupsDetailForUser 메서드 (userId: " + userId + ") ---");

        List<AlbumMembers> albumMemberships = albumMembersRepository.findByUserEntity_UserId(userId);
        List<OurAlbumResponseDefaultDto> allGroupDetails = new ArrayList<>();

        System.out.println("  [정보] 사용자 ID " + userId + " 가 속한 그룹 수: " + albumMemberships.size());
        if (albumMemberships.isEmpty()) {
            System.out.println("  [경고] 해당 사용자는 어떤 그룹에도 속해 있지 않습니다. 빈 리스트를 반환합니다.");
            return new ArrayList<>();
        }

        for (AlbumMembers membership : albumMemberships) {
            OurAlbum ourAlbumFromMembership = membership.getOurAlbum();
            Integer groupIdInt = ourAlbumFromMembership.getAlbumId();

            System.out.println("\n--- [그룹 시작] 그룹 ID: " + groupIdInt + ", 그룹명: " + ourAlbumFromMembership.getAlbumName() + " ---");

            List<OurAlbumResponseDefaultDto.Member> membersDto = albumMembersRepository.findByOurAlbum_AlbumId(groupIdInt)
                    .stream()
                    .map(member -> OurAlbumResponseDefaultDto.Member.builder()
                            .userId(member.getUserEntity().getUserId())
                            .userName(member.getUserEntity().getUserName())
                            .userPhotourl(member.getUserEntity().getUserPhotourl())
                            .build())
                    .collect(Collectors.toList());
            System.out.println("  [정보] 그룹 ID " + groupIdInt + " 의 멤버 수: " + membersDto.size());

            List<OurAlbum> ourAlbums = ourAlbumRepository.findAllByAlbumIdWithPostsAndPhotos(groupIdInt);
            List<OurAlbumResponseDefaultDto.Album> albumsDto = new ArrayList<>();

            System.out.println("  [정보] 그룹 ID " + groupIdInt + " 에 연결된 앨범 수: " + ourAlbums.size());
            if (ourAlbums.isEmpty()) {
                System.out.println("  [경고] 이 그룹에는 연결된 앨범이 없습니다.");
            }

            for (OurAlbum currentAlbum : ourAlbums) {
                System.out.println("\n---- [앨범 시작] 앨범 ID: " + (currentAlbum.getAlbumId() != null ? currentAlbum.getAlbumId().longValue() : "NULL") + ", 앨범명: " + currentAlbum.getAlbumName() + " ----");

                List<OurPost> posts = new ArrayList<>(currentAlbum.getPosts());
                List<OurAlbumResponseDefaultDto.Photo> photosDto = new ArrayList<>();
                // photoIds Set은 여전히 Integer를 저장합니다.
                Set<Integer> photoIds = new HashSet<>();
                Set<String> photoUrls = new HashSet<>();

                System.out.println("    [정보] 앨범 ID " + (currentAlbum.getAlbumId() != null ? currentAlbum.getAlbumId().longValue() : "NULL") + " 에 포함된 게시물 수: " + posts.size());
                if (posts.isEmpty()) {
                    System.out.println("    [경고] 이 앨범에는 연결된 게시물이 없습니다.");
                }

                for (OurPost ourPost : posts) {
                    System.out.println("      [게시물 처리] 앨범 ID: " + (currentAlbum.getAlbumId() != null ? currentAlbum.getAlbumId().longValue() : "NULL") + ", 게시물 ID: " + (ourPost.getPostId() != null ? ourPost.getPostId().longValue() : "NULL"));

                    List<Photo> photos = new ArrayList<>(ourPost.getPhotos());

                    System.out.println("      [사진 검색 결과] 게시물 ID " + (ourPost.getPostId() != null ? ourPost.getPostId().longValue() : "NULL") + " 에 대해 찾은 사진 수 (페치 조인): " + photos.size());

                    if (photos.isEmpty()) {
                        System.out.println("        [경고] 이 게시물에는 연결된 사진이 없습니다.");
                    } else {
                        for (Photo photo : photos) {
                            if (photo.getPhotoId() != null) {
                                String originalUrl = photo.getPhotoUrl();
                                String photoUrl = null;

                                if (originalUrl != null) {
                                    if (originalUrl.startsWith("http://") || originalUrl.startsWith("https://")) {
                                        photoUrl = originalUrl;
                                    } else {
                                        photoUrl = s3UrlResponseService.getFileUrl(originalUrl);
                                    }
                                }

                                if (!photoIds.contains(photo.getPhotoId()) && (photoUrl == null || !photoUrls.contains(photoUrl))) {
                                    photoIds.add(photo.getPhotoId());
                                    if (photoUrl != null) photoUrls.add(photoUrl);

                                    photosDto.add(OurAlbumResponseDefaultDto.Photo.builder()
                                            // --- 이 부분을 수정합니다! Integer를 Long으로 변환 ---
                                            .photoId(photo.getPhotoId().longValue()) // <--- photo.getPhotoId()를 Long으로 변환!
                                            .photoUrl(photoUrl)
                                            .photoName(photo.getPhotoName())
                                            .postId(ourPost.getPostId() != null ? ourPost.getPostId().longValue() : null)
                                            .photoMakingtime(photo.getPhotoMakingTime() != null ? photo.getPhotoMakingTime().toLocalDate().toString() : null)
                                            .build());

                                    System.out.println("          [사진 추가 성공] Photo ID: " + photo.getPhotoId() + ", Photo URL: " + (photoUrl != null ? photoUrl : "NULL"));
                                } else {
                                    System.out.println("          [중복 사진 무시] Photo ID: " + photo.getPhotoId());
                                }
                            } else {
                                System.out.println("          [경고] Photo ID가 NULL인 사진 발견, 무시합니다.");
                            }
                        }
                    }
                }

                List<Long> postIdsInAlbum = posts.stream()
                        .map(OurPost::getPostId)
                        .filter(postId -> postId != null)
                        .map(Integer::longValue)
                        .collect(Collectors.toList());

                List<Comment> commentsInAlbum = new ArrayList<>();
                if (!postIdsInAlbum.isEmpty()) {
                    commentsInAlbum = commentRepository.findByOurPost_PostIdIn(postIdsInAlbum);
                    System.out.println("    [정보] 앨범 ID " + (currentAlbum.getAlbumId() != null ? currentAlbum.getAlbumId().longValue() : "NULL") + " 의 게시물들에 대한 댓글 수: " + commentsInAlbum.size());
                } else {
                    System.out.println("    [정보] 앨범 ID " + (currentAlbum.getAlbumId() != null ? currentAlbum.getAlbumId().longValue() : "NULL") + " 에 게시물이 없어 댓글을 조회하지 않습니다.");
                }

                List<OurAlbumResponseDefaultDto.Comment> commentsDto = commentsInAlbum.stream()
                        .map(comment -> OurAlbumResponseDefaultDto.Comment.builder()
                                .albumId(comment.getOurAlbum() != null ? comment.getOurAlbum().getAlbumId().longValue() : null)
                                .photoId(comment.getOurPost() != null ? comment.getOurPost().getPostId().longValue() : null)
                                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                                .commentText(comment.getCommentText())
                                .build())
                        .collect(Collectors.toList());

                albumsDto.add(OurAlbumResponseDefaultDto.Album.builder()
                        .albumId(currentAlbum.getAlbumId() != null ? currentAlbum.getAlbumId().longValue() : null)
                        .albumName(currentAlbum.getAlbumName())
                        .albumDescription(currentAlbum.getAlbumDescription())
                        .albumTag(currentAlbum.getAlbumTag())
                        .albumMakingtime(currentAlbum.getAlbumMakingTime() != null ? currentAlbum.getAlbumMakingTime().toLocalDate().toString() : null)
                        .photos(photosDto)
                        .comments(commentsDto)
                        .build());

                System.out.println("---- [앨범 완료] 앨범 ID: " + (currentAlbum.getAlbumId() != null ? currentAlbum.getAlbumId().longValue() : "NULL") + " ----");
            }

            allGroupDetails.add(OurAlbumResponseDefaultDto.builder()
                    .groupId(ourAlbumFromMembership.getAlbumId() != null ? ourAlbumFromMembership.getAlbumId().longValue() : null)
                    .groupName(ourAlbumFromMembership.getAlbumName())
                    .members(membersDto)
                    .albums(albumsDto)
                    .build());

            System.out.println("--- [그룹 완료] 그룹 ID: " + groupIdInt + " ---");
        }

        System.out.println("\n--- [종료] getAllGroupsDetailForUser 메서드 ---");
        return allGroupDetails;
    }
}
