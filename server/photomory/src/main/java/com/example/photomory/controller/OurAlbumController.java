package com.example.photomory.controller;

import com.example.photomory.dto.*;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.security.CustomUserDetails;
import com.example.photomory.service.OurAlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.photomory.exception.UnauthorizedException;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/our-album")
public class OurAlbumController {

    private final OurAlbumService ourAlbumService;
    private final ObjectMapper objectMapper;

    private UserEntity getAuthenticatedUser(CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        return userDetails.getUser();
    }

    // 0. 기본 페이지 데이터 조회
    @GetMapping
    public ResponseEntity<List<OurAlbumResponseDefaultDto>> getAllUserGroupsDefaultData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = getAuthenticatedUser(userDetails);
        List<OurAlbumResponseDefaultDto> userGroupsDetail = ourAlbumService.getAllGroupsDetailForUser(user.getUserId());
        return ResponseEntity.ok(userGroupsDetail);
    }

    // 1. 그룹 생성
    @PostMapping("/group")
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody @Valid GroupCreateRequestDto requestDto,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = getAuthenticatedUser(userDetails);
        GroupResponseDto responseDto = ourAlbumService.createGroup(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 2. 그룹 정보 + 구성원 반환
    @GetMapping("/group/{groupId}")
    public ResponseEntity<OurAlbumResponseDefaultDto> getGroupFullInfo(@PathVariable("groupId") Long groupId) {
        OurAlbumResponseDefaultDto responseDto = ourAlbumService.getGroupFullInfo(groupId);
        return ResponseEntity.ok(responseDto);
    }

    // 3. 앨범 생성
    @PostMapping("/group/{groupId}/album")
    public ResponseEntity<AlbumResponseDto> createAlbum(@PathVariable("groupId") Long groupId,
                                                        @RequestBody @Valid AlbumCreateRequestDto requestDto,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = getAuthenticatedUser(userDetails);
        AlbumResponseDto responseDto = ourAlbumService.createAlbum(groupId, requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 4. 앨범 상세정보 + 포스트 목록
    @GetMapping("/album/{albumId}")
    public ResponseEntity<AlbumWithPostsResponseDto> getAlbumWithPosts(
            @PathVariable("albumId") Integer albumId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        AlbumWithPostsResponseDto responseDto = ourAlbumService.getAlbumWithPosts(albumId, page, size);
        return ResponseEntity.ok(responseDto);
    }

    // 5. 게시물 생성 (파일 포함)
    @PostMapping("/album/{albumId}/post")
    public ResponseEntity<PostResponseDto> createPost(
            @PathVariable("albumId") Integer albumId,
            @RequestPart(name = "requestDtoJson") String requestDtoJson,
            @RequestPart(name = "photo", required = false) MultipartFile photo,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        UserEntity user = getAuthenticatedUser(userDetails);

        PostCreateRequestDto requestDto;
        try {
            requestDto = objectMapper.readValue(requestDtoJson, PostCreateRequestDto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid PostCreateRequestDto JSON: " + e.getMessage(), e);
        }

        PostResponseDto responseDto = ourAlbumService.createPost(albumId, requestDto, photo, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 7. 댓글 작성
    @PostMapping("/{albumId}/post/{postId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable("albumId") Integer albumId,
            @PathVariable("postId") Integer postId,
            @RequestBody @Valid CommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserEntity user = getAuthenticatedUser(userDetails);

        if (!albumId.equals(requestDto.getAlbumId()) || !postId.equals(requestDto.getPostId())) {
            throw new IllegalArgumentException("요청 경로와 본문의 앨범 또는 게시글 ID가 일치하지 않습니다.");
        }

        CommentResponseDto response = ourAlbumService.createComment(albumId, postId, user, requestDto.getCommentsText());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 9. 초대 가능한 친구 목록 조회
    @GetMapping("/group/{groupId}/invitable-friends")
    public ResponseEntity<List<UserSummaryDto>> getInvitableFriends(@PathVariable("groupId") Long groupId,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = getAuthenticatedUser(userDetails);
        List<UserSummaryDto> friends = ourAlbumService.getFriendsExcludingGroup(groupId, user.getUserId());
        return ResponseEntity.ok(friends);
    }

    // 10. 친구를 그룹에 초대
    @PostMapping("/group/{groupId}/invite")
    public ResponseEntity<String> inviteToGroup(@PathVariable("groupId") Long groupId,
                                                @RequestBody List<Long> friendIds,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity inviter = getAuthenticatedUser(userDetails);
        ourAlbumService.inviteToGroup(groupId, inviter, friendIds);
        return ResponseEntity.ok("친구 초대가 완료되었습니다.");
    }

    // 11. 친구 그룹에서 삭제
    @DeleteMapping("/group/{groupId}/member/{userIdToRemove}")
    public ResponseEntity<Void> removeMemberFromGroup(@PathVariable("groupId") Long groupId,
                                                      @PathVariable("userIdToRemove") Long userIdToRemove,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity currentUser = getAuthenticatedUser(userDetails);
        ourAlbumService.removeMemberFromGroup(groupId, userIdToRemove, currentUser);
        return ResponseEntity.noContent().build();
    }

    // 12. 특정 앨범에서 게시글 삭제
    @DeleteMapping("/album/{albumId}/post/{postId}")
    public ResponseEntity<Void> deletePostInAlbum(@PathVariable("albumId") Integer albumId,
                                                  @PathVariable("postId") Integer postId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity currentUser = getAuthenticatedUser(userDetails);
        ourAlbumService.deletePostWithFile(albumId, postId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // 13. 앨범 삭제
    @DeleteMapping("/album/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable("albumId") Long albumId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity currentUser = getAuthenticatedUser(userDetails);
        ourAlbumService.deleteAlbum(albumId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
