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
    // 특정앨범에서 게시글 삭제
    @Transactional
    public void deletePostInAlbum(Long albumId, Long postId, UserEntity currentUser) {
        // ID 타입 변환 (Long -> Integer)
        Integer albumIdInt = albumId.intValue();
        Integer postIdInt = postId.intValue();

        // 1. 앨범 존재 여부 확인 (선택 사항: 앨범 존재 확인이 꼭 필요한지는 요구사항에 따라 다름)
        Album album = albumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("해당 앨범을 찾을 수 없습니다."));

        // 2. 게시글 존재 여부 확인
        Post post = postRepository.findById(postIdInt)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        // 3. 게시글이 해당 앨범에 속하는지 확인
        // (post.getAlbum().getAlbumId()가 Integer 타입인지 확인 필요)
        if (!post.getAlbum().getAlbumId().equals(albumIdInt)) {
            throw new IllegalArgumentException("요청된 게시글이 해당 앨범에 속하지 않습니다.");
        }

        // 4. 게시글 삭제 권한 확인 (작성자만 삭제 가능하도록)
        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다. (작성자만 삭제 가능)");
        }

        // 5. S3에 저장된 사진이 있다면 삭제
        if (post.getPhotoUrl() != null && !post.getPhotoUrl().isEmpty()) {
            s3Service.deleteFile(post.getPhotoUrl());
        }

        // 6. 게시글 삭제
        postRepository.delete(post);
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

        // 이 줄을 추가해야 합니다!
        comment.setCommentTime(LocalDateTime.now()); // 현재 시간을 설정

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

    // 앨범 삭제
    @Transactional
    public void deleteAlbum(Long albumId, UserEntity currentUser) {
        // ID 타입 변환 (Long -> Integer)
        Integer albumIdInt = albumId.intValue();

        // 1. 앨범 존재 여부 확인
        Album album = albumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("해당 앨범을 찾을 수 없습니다."));

        // 2. 앨범 삭제 권한 확인 (앨범 생성자만 삭제 가능하도록)
        if (!album.getMyAlbum().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("앨범 삭제 권한이 없습니다. (앨범을 생성한 그룹의 관리자만 삭제 가능)");
        }

        // 3. 앨범에 속한 모든 게시글을 찾아서 삭제 (중요: cascade가 없으면 수동 삭제)
        List<Post> postsToDelete = postRepository.findByAlbum_AlbumId(albumIdInt);

        for (Post post : postsToDelete) {
            // 해당 게시글에 달린 모든 댓글 삭제
            // CommentRepository는 Post ID를 Long으로 받으므로, post.getPostId()를 Long으로 변환 (Post의 ID는 Integer)
            commentRepository.deleteByPost_PostId(post.getPostId().longValue()); // post.getPostId()가 Integer이므로 longValue() 호출

            // S3에 저장된 사진이 있다면 삭제
            if (post.getPhotoUrl() != null && !post.getPhotoUrl().isEmpty()) {
                s3Service.deleteFile(post.getPhotoUrl());
            }
            // 게시글 삭제 (PostRepository는 Integer ID를 기대)
            postRepository.delete(post);
        }

        // 4. 앨범 자체 삭제
        albumRepository.delete(album);
    }

    // 우리의 추억 페이지 기본 데이터 반환
    @Transactional(readOnly = true)
    public List<OurAlbumResponseDefaultDto> getAllGroupsDetailForUser(Long userId) {
        // 1. 해당 사용자가 멤버로 속한 모든 MyAlbum (그룹) 조회
        List<AlbumMembers> albumMemberships = albumMembersRepository.findByUserEntity_UserId(userId);

        List<OurAlbumResponseDefaultDto> allGroupDetails = new ArrayList<>();

        for (AlbumMembers membership : albumMemberships) {
            MyAlbum myAlbum = membership.getMyAlbum();
            Integer groupIdInt = myAlbum.getMyalbumId();

            // 1.1. 그룹 멤버 정보 가져오기
            List<OurAlbumResponseDefaultDto.Member> membersDto = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                    .stream()
                    .map(member -> OurAlbumResponseDefaultDto.Member.builder()
                            .userId(member.getUserEntity().getUserId())
                            .userName(member.getUserEntity().getUserName())
                            .userPhotourl(member.getUserEntity().getUserPhotourl())
                            .build())
                    .collect(Collectors.toList());

            // 1.2. 그룹에 속한 모든 앨범 정보 가져오기
            // AlbumRepository에 findByMyAlbum_MyalbumId(Integer myAlbumId) 메소드가 필요합니다.
            List<Album> albums = albumRepository.findByMyAlbum_MyalbumId(groupIdInt);

            List<OurAlbumResponseDefaultDto.Album> albumsDto = new ArrayList<>();
            for (Album album : albums) {
                // 1.2.1. 각 앨범에 속한 게시글(사진) 정보 가져오기
                List<Post> posts = postRepository.findByAlbum_AlbumId(album.getAlbumId());

                List<OurAlbumResponseDefaultDto.Photo> photosDto = posts.stream()
                        .map(post -> OurAlbumResponseDefaultDto.Photo.builder()
                                .photoId(post.getPostId() != null ? post.getPostId().longValue() : null) // Integer -> Long
                                .photoUrl(post.getPhotoUrl())
                                .photoName(post.getPostDescription()) // postDescription을 photo_name으로 사용
                                .postId(post.getPostId() != null ? post.getPostId().longValue() : null) // Integer -> Long
                                .photoMakingtime(post.getMakingTime() != null ? post.getMakingTime().toLocalDate().toString() : null) // LocalDateTime -> String
                                .build())
                        .collect(Collectors.toList());

                // 1.2.2. 각 앨범에 속한 모든 댓글 정보 가져오기
                List<Long> postIdsInAlbum = posts.stream()
                        .map(Post::getPostId)
                        .map(Integer::longValue) // PostId는 Integer, CommentRepository는 Long을 받으므로 변환
                        .collect(Collectors.toList());

                List<Comment> commentsInAlbum = new ArrayList<>();
                if (!postIdsInAlbum.isEmpty()) {
                    commentsInAlbum = commentRepository.findByPost_PostIdIn(postIdsInAlbum);
                }

                List<OurAlbumResponseDefaultDto.Comment> commentsDto = commentsInAlbum.stream()
                        .map(comment -> OurAlbumResponseDefaultDto.Comment.builder()
                                .albumId(comment.getAlbum() != null ? comment.getAlbum().getAlbumId().longValue() : null) // Integer -> Long
                                .photoId(comment.getPost() != null ? comment.getPost().getPostId().longValue() : null) // Integer -> Long
                                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                                .commentText(comment.getCommentsText())
                                .build())
                        .collect(Collectors.toList());

                albumsDto.add(OurAlbumResponseDefaultDto.Album.builder()
                        .albumId(album.getAlbumId() != null ? album.getAlbumId().longValue() : null) // Integer -> Long
                        .albumName(album.getAlbumName())
                        .albumDescription(album.getAlbumDescription())
                        .albumTag(album.getAlbumTag())
                        .albumMakingtime(album.getAlbumMakingTime() != null ? album.getAlbumMakingTime().toLocalDate().toString() : null) // LocalDateTime -> String
                        .photos(photosDto)
                        .comments(commentsDto)
                        .build());
            }

            allGroupDetails.add(OurAlbumResponseDefaultDto.builder()
                    .groupId(myAlbum.getMyalbumId() != null ? myAlbum.getMyalbumId().longValue() : null) // Integer -> Long
                    .groupName(myAlbum.getMyalbumName())
                    .members(membersDto)
                    .albums(albumsDto)
                    .build());
        }
        return allGroupDetails;
    }
}