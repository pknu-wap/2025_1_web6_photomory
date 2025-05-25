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

    private final AlbumRepository albumRepository;
    private final AlbumMembersRepository albumMembersRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MyAlbumRepository myAlbumRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;

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

        post.setPostText(requestDto.getPostTitle());


        post.setMakingTime(requestDto.getPostTime());

        if (photoFile != null && !photoFile.isEmpty()) {
            String photoUrl = s3Service.uploadFile(photoFile);
            post.setPhotoUrl(photoUrl);
        } else if (requestDto.getPostImageUrl() != null) {
            // 요청 DTO에 이미지 URL이 있으면 그걸 설정
            post.setPhotoUrl(requestDto.getPostImageUrl());
        }

        Post savedPost = postRepository.save(post);
        return PostResponseDto.fromEntity(savedPost);
    }


    // 특정앨범에서 게시글 삭제
    @Transactional
    public void deletePostInAlbum(Long albumId, Long postId, UserEntity currentUser) {
        Integer albumIdInt = albumId.intValue();
        Integer postIdInt = postId.intValue();

        Album album = albumRepository.findById(albumIdInt)
                .orElseThrow(() -> new EntityNotFoundException("해당 앨범을 찾을 수 없습니다."));

        Post post = postRepository.findById(postIdInt)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));

        if (!post.getAlbum().getAlbumId().equals(albumIdInt)) {
            throw new IllegalArgumentException("요청된 게시글이 해당 앨범에 속하지 않습니다.");
        }

        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다. (작성자만 삭제 가능)");
        }

        if (post.getPhotoUrl() != null && !post.getPhotoUrl().isEmpty()) {
            s3Service.deleteFile(post.getPhotoUrl());
        }

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
    public void removeMemberFromGroup(Long groupId, Long userIdToRemove) {
        Integer groupIdInt = groupId.intValue();

        MyAlbum group = myAlbumRepository.findById(groupIdInt)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다."));

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

        if (!album.getMyAlbum().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("앨범 삭제 권한이 없습니다. (앨범을 생성한 그룹의 관리자만 삭제 가능)");
        }

        List<Post> postsToDelete = postRepository.findByAlbum_AlbumId(albumIdInt);

        for (Post post : postsToDelete) {
            commentRepository.deleteByPost_PostId(post.getPostId().longValue());
            if (post.getPhotoUrl() != null && !post.getPhotoUrl().isEmpty()) {
                s3Service.deleteFile(post.getPhotoUrl());
            }
            postRepository.delete(post);
        }

        albumRepository.delete(album);
    }

    // 우리의 추억 페이지 기본 데이터 반환
    @Transactional(readOnly = true)
    public List<OurAlbumResponseDefaultDto> getAllGroupsDetailForUser(Long userId) {
        List<AlbumMembers> albumMemberships = albumMembersRepository.findByUserEntity_UserId(userId);

        List<OurAlbumResponseDefaultDto> allGroupDetails = new ArrayList<>();

        for (AlbumMembers membership : albumMemberships) {
            MyAlbum myAlbum = membership.getMyAlbum();
            Integer groupIdInt = myAlbum.getMyalbumId();

            List<OurAlbumResponseDefaultDto.Member> membersDto = albumMembersRepository.findByMyAlbum_MyalbumId(groupIdInt)
                    .stream()
                    .map(member -> OurAlbumResponseDefaultDto.Member.builder()
                            .userId(member.getUserEntity().getUserId())
                            .userName(member.getUserEntity().getUserName())
                            .userPhotourl(member.getUserEntity().getUserPhotourl())
                            .build())
                    .collect(Collectors.toList());

            List<Album> albums = albumRepository.findByMyAlbum_MyalbumId(groupIdInt);

            List<OurAlbumResponseDefaultDto.Album> albumsDto = new ArrayList<>();
            for (Album album : albums) {
                List<Post> posts = postRepository.findByAlbum_AlbumId(album.getAlbumId());

                List<OurAlbumResponseDefaultDto.Photo> photosDto = posts.stream()
                        .map(post -> OurAlbumResponseDefaultDto.Photo.builder()
                                .photoId(post.getPostId() != null ? post.getPostId().longValue() : null)
                                .photoUrl(post.getPhotoUrl())
                                .photoName(post.getPostDescription())
                                .postId(post.getPostId() != null ? post.getPostId().longValue() : null)
                                .photoMakingtime(post.getMakingTime() != null ? post.getMakingTime().toLocalDate().toString() : null)
                                .build())
                        .collect(Collectors.toList());

                List<Long> postIdsInAlbum = posts.stream()
                        .map(Post::getPostId)
                        .map(Integer::longValue)
                        .collect(Collectors.toList());

                List<Comment> commentsInAlbum = new ArrayList<>();
                if (!postIdsInAlbum.isEmpty()) {
                    commentsInAlbum = commentRepository.findByPost_PostIdIn(postIdsInAlbum);
                }

                List<OurAlbumResponseDefaultDto.Comment> commentsDto = commentsInAlbum.stream()
                        .map(comment -> OurAlbumResponseDefaultDto.Comment.builder()
                                .albumId(comment.getAlbum() != null ? comment.getAlbum().getAlbumId().longValue() : null)
                                .photoId(comment.getPost() != null ? comment.getPost().getPostId().longValue() : null)
                                .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                                .commentText(comment.getCommentsText())
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
            }

            allGroupDetails.add(OurAlbumResponseDefaultDto.builder()
                    .groupId(myAlbum.getMyalbumId() != null ? myAlbum.getMyalbumId().longValue() : null)
                    .groupName(myAlbum.getMyalbumName())
                    .members(membersDto)
                    .albums(albumsDto)
                    .build());
        }
        return allGroupDetails;
    }
}