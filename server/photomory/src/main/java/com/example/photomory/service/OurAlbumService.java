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
import java.time.LocalDateTime;
import java.util.ArrayList;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OurAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMembersRepository albumMembersRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MyAlbumRepository myAlbumRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final S3UrlResponseService s3UrlResponseService;
    private final PhotoRepository photoRepository;


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
        Integer groupIdInt = groupId.intValue();

        MyAlbum group = myAlbumRepository.findById(groupIdInt)
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

        MyAlbum group = myAlbumRepository.findById(groupIdInt)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));

        Album album = new Album();
        album.setAlbumName(requestDto.getAlbumName());

        // 리스트 -> 콤마로 구분된 문자열 변환 후 저장
        if (requestDto.getAlbumTags() != null && !requestDto.getAlbumTags().isEmpty()) {
            String tags = String.join(",", requestDto.getAlbumTags());
            album.setAlbumTag(tags);
        } else {
            album.setAlbumTag(null);
        }

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
    public PostResponseDto createPost(Long albumId, PostCreateRequestDto requestDto, MultipartFile photoFile, Long userId) throws IOException {
        Integer albumIdInt = albumId.intValue();

        Album album = albumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Post post = new Post();
        post.setAlbum(album);
        post.setUser(user);
        post.setPostText(requestDto.getPostTitle());
        if (requestDto.getPostTime() != null) {
            post.setMakingTime(requestDto.getPostTime());
        } else {
            post.setMakingTime(LocalDateTime.now());
        }

        Post savedPost = postRepository.save(post);

        if (photoFile != null && !photoFile.isEmpty()) {
            String uploadedUrl = s3Service.uploadFile(photoFile);

            // savedPost.setPhotoUrl(uploadedUrl); // Post 엔티티의 photoUrl 필드는 필요 없을 수 있습니다.
            // Photo 엔티티가 사진 정보를 담당하도록 합니다.
            // 만약 Post에 대표 사진 URL이 필요하다면 유지합니다.

            Photo photo = new Photo();
            photo.setPost(savedPost);
            photo.setPhotoUrl(uploadedUrl);
            photo.setPhotoName(requestDto.getPhotoName()); // 추가: DTO에서 받은 사진 이름 설정
            photo.setPhotoMakingTime(requestDto.getPhotoMakingTime());
            photoRepository.save(photo);
        }

        return PostResponseDto.fromEntity(savedPost);
    }

    // 게시글 삭제
    @Transactional
    public void deletePostWithFile(Integer albumId, Integer postId, UserEntity currentUser) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("앨범을 찾을 수 없습니다. id=" + albumId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        if (!post.getAlbum().equals(album)) {
            throw new IllegalArgumentException("게시글이 해당 앨범에 속하지 않습니다.");
        }

        if (post.getPhotos() != null) {
            for (Photo photo : post.getPhotos()) {
                if (photo.getPhotoUrl() != null && !photo.getPhotoUrl().isEmpty()) {
                    s3Service.deleteFile(photo.getPhotoUrl());
                }
            }
        }

        postRepository.delete(post);
    }


    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Integer albumId, Integer postId, UserEntity user, String text) {
        // 앨범과 게시글이 존재하는지 확인
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 유저 정보 유효성 검사 (Comment 엔티티의 user 필드가 nullable=false이므로 필수)
        if (user == null) {
            throw new IllegalArgumentException("댓글을 작성할 사용자 정보가 유효하지 않습니다.");
        }

        // 댓글 내용 길이 검사 (Comment 엔티티의 commentsText 필드가 length=500으로 제한되어 있음)
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비워둘 수 없습니다.");
        }
        if (text.length() > 500) {
            throw new IllegalArgumentException("댓글 내용은 500자를 초과할 수 없습니다.");
        }

        Comment comment = new Comment();
        comment.setAlbum(album);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCommentText(text);
        comment.setCommentTime(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.fromEntity(saved);
    }
    // 친구 중에서 그룹에 없는 사람만 필터링하여 초대하기
    @Transactional(readOnly = true)
    public List<UserSummaryDto> getInvitableFriends(Long groupId, Long userId) {
        Integer groupIdInt = groupId.intValue();

        List<Friend> friends = friendRepository.findByFromUserIdAndAreWeFriendTrue(userId);

        List<UserEntity> friendUsers = friends.stream()
                .map(friend -> userRepository.findById(friend.getToUserId()).orElse(null))
                .filter(user -> user != null)
                .collect(Collectors.toList());

        List<UserEntity> groupMembers = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                .stream()
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
                .filter(user -> user != null)
                .toList();

        List<UserEntity> groupMembers = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt).stream()
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
        Integer groupIdInt = groupId.intValue();

        MyAlbum group = myAlbumRepository.findById(groupIdInt)
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
    public void removeMemberFromGroup(Long groupId, Long userIdToRemove, UserEntity currentUser) {
        Integer groupIdInt = groupId.intValue();

        // 그룹 조회
        MyAlbum group = myAlbumRepository.findById(groupIdInt)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));

        // 삭제할 멤버 조회
        AlbumMembers memberToRemove = albumMembersRepository.findByMyAlbum_MyalbumIdAndUserEntity_UserId(groupIdInt, userIdToRemove)
                .orElseThrow(() -> new EntityNotFoundException("그룹에서 해당 멤버를 찾을 수 없습니다."));

        albumMembersRepository.delete(memberToRemove);
    }


    // 앨범 삭제
    @Transactional
    public void deleteAlbum(Long albumId, UserEntity currentUser) {
        Integer albumIdInt = albumId.intValue();

        Album album = albumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("해당 앨범을 찾을 수 없습니다."));

        // 권한 확인 로직은 유지
        if (!album.getMyAlbum().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("앨범 삭제 권한이 없습니다. (앨범을 생성한 그룹의 관리자만 삭제 가능)");
        }

        // 해당 앨범에 속한 모든 게시물 조회
        List<Post> postsToDelete = postRepository.findByAlbum_AlbumId(albumIdInt);

        for (Post post : postsToDelete) {
            // 1. 게시물에 연결된 모든 댓글 삭제
            commentRepository.deleteByPost_PostId(post.getPostId().longValue());

            if (post.getPhotos() != null && !post.getPhotos().isEmpty()) {
                for (Photo photo : post.getPhotos()) {
                    if (photo.getPhotoUrl() != null && !photo.getPhotoUrl().isEmpty()) {
                        s3Service.deleteFile(photo.getPhotoUrl());
                    }
                }
            }

            postRepository.delete(post);
        }
        // 앨범 삭제
        albumRepository.delete(album); // <- 여기도 albumRepository.delete(album); 으로 끝나야 합니다.
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
            MyAlbum myAlbum = membership.getMyAlbum();
            Integer groupIdInt = myAlbum.getMyalbumId();

            System.out.println("\n--- [그룹 시작] 그룹 ID: " + groupIdInt + ", 그룹명: " + myAlbum.getMyalbumName() + " ---");

            List<OurAlbumResponseDefaultDto.Member> membersDto = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                    .stream()
                    .map(member -> OurAlbumResponseDefaultDto.Member.builder()
                            .userId(member.getUserEntity().getUserId())
                            .userName(member.getUserEntity().getUserName())
                            .userPhotourl(member.getUserEntity().getUserPhotourl())
                            .build())
                    .collect(Collectors.toList());
            System.out.println("  [정보] 그룹 ID " + groupIdInt + " 의 멤버 수: " + membersDto.size());

            // 앨범, 게시물, 사진이 모두 페치 조인으로 미리 로딩됨
            List<Album> albums = albumRepository.findAllByMyAlbumIdWithPostsAndPhotos(groupIdInt);
            List<OurAlbumResponseDefaultDto.Album> albumsDto = new ArrayList<>();

            System.out.println("  [정보] 그룹 ID " + groupIdInt + " 에 연결된 앨범 수: " + albums.size());
            if (albums.isEmpty()) {
                System.out.println("  [경고] 이 그룹에는 연결된 앨범이 없습니다.");
            }

            for (Album album : albums) {
                System.out.println("\n---- [앨범 시작] 앨범 ID: " + (album.getAlbumId() != null ? album.getAlbumId().longValue() : "NULL") + ", 앨범명: " + album.getAlbumName() + " ----");

                List<Post> posts = new ArrayList<>(album.getPosts()); // 이미 페치 조인된 posts 컬렉션
                List<OurAlbumResponseDefaultDto.Photo> photosDto = new ArrayList<>();

                System.out.println("    [정보] 앨범 ID " + (album.getAlbumId() != null ? album.getAlbumId().longValue() : "NULL") + " 에 포함된 게시물 수: " + posts.size());
                if (posts.isEmpty()) {
                    System.out.println("    [경고] 이 앨범에는 연결된 게시물이 없습니다.");
                }

                for (Post post : posts) {
                    System.out.println("      [게시물 처리] 앨범 ID: " + (album.getAlbumId() != null ? album.getAlbumId().longValue() : "NULL") + ", 게시물 ID: " + (post.getPostId() != null ? post.getPostId().longValue() : "NULL"));

                    // *** 여기가 수정된 핵심 부분입니다: Set<Photo>를 List<Photo>로 변환 ***
                    List<Photo> photos = new ArrayList<>(post.getPhotos()); // Post 엔티티의 photos 필드가 Set이므로 List로 변환

                    System.out.println("      [사진 검색 결과] 게시물 ID " + (post.getPostId() != null ? post.getPostId().longValue() : "NULL") + " 에 대해 찾은 사진 수 (페치 조인): " + photos.size());

                    if (photos.isEmpty()) {
                        System.out.println("        [경고] 이 게시물에는 연결된 사진이 없습니다.");
                    } else {
                        for (Photo photo : photos) {
                            photosDto.add(OurAlbumResponseDefaultDto.Photo.builder()
                                    .photoId(photo.getPhotoId())
                                    .photoUrl(photo.getPhotoUrl() != null ? s3UrlResponseService.getFileUrl(photo.getPhotoUrl()) : null)
                                    .photoName(photo.getPhotoName())
                                    .postId(post.getPostId() != null ? post.getPostId().longValue() : null)
                                    .photoMakingtime(photo.getPhotoMakingTime() != null ? photo.getPhotoMakingTime().toLocalDate().toString() : null)
                                    .build());
                            System.out.println("          [사진 추가 성공] Photo ID: " + photo.getPhotoId() + ", Photo URL: " + (photo.getPhotoUrl() != null ? photo.getPhotoUrl() : "NULL"));
                        }
                    }
                }

                List<Long> postIdsInAlbum = posts.stream()
                        .map(Post::getPostId)
                        .map(Integer::longValue)
                        .collect(Collectors.toList());

                List<Comment> commentsInAlbum = new ArrayList<>();
                if (!postIdsInAlbum.isEmpty()) {
                    commentsInAlbum = commentRepository.findByPost_PostIdIn(postIdsInAlbum);
                    System.out.println("    [정보] 앨범 ID " + (album.getAlbumId() != null ? album.getAlbumId().longValue() : "NULL") + " 의 게시물들에 대한 댓글 수: " + commentsInAlbum.size());
                } else {
                    System.out.println("    [정보] 앨범 ID " + (album.getAlbumId() != null ? album.getAlbumId().longValue() : "NULL") + " 에 게시물이 없어 댓글을 조회하지 않습니다.");
                }

                List<OurAlbumResponseDefaultDto.Comment> commentsDto = commentsInAlbum.stream()
                        .map(comment -> OurAlbumResponseDefaultDto.Comment.builder()
                                .albumId(comment.getAlbum() != null ? comment.getAlbum().getAlbumId().longValue() : null)
                                .photoId(comment.getPost() != null ? comment.getPost().getPostId().longValue() : null)
                                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                                .commentText(comment.getCommentText())
                                .build())
                        .collect(Collectors.toList());

                albumsDto.add(OurAlbumResponseDefaultDto.Album.builder()
                        .albumId(album.getAlbumId() != null ? album.getAlbumId().longValue() : null)
                        .albumName(album.getAlbumName())
                        .albumDescription(album.getAlbumDescription())
                        .albumTag(album.getAlbumTag())
                        .albumMakingtime(album.getAlbumMakingTime() != null ? album.getAlbumMakingTime().toLocalDate().toString() : null)
                        .photos(photosDto)
                        .comments(commentsDto)
                        .build());
                System.out.println("---- [앨범 완료] 앨범 ID: " + (album.getAlbumId() != null ? album.getAlbumId().longValue() : "NULL") + " ----");
            }

            allGroupDetails.add(OurAlbumResponseDefaultDto.builder()
                    .groupId(myAlbum.getMyalbumId() != null ? myAlbum.getMyalbumId().longValue() : null)
                    .groupName(myAlbum.getMyalbumName())
                    .members(membersDto)
                    .albums(albumsDto)
                    .build());
            System.out.println("--- [그룹 완료] 그룹 ID: " + groupIdInt + " ---");
        }

        System.out.println("\n--- [종료] getAllGroupsDetailForUser 메서드 ---");
        return allGroupDetails;
    }
}
