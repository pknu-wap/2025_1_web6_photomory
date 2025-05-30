package com.example.photomory.controller;

import com.example.photomory.dto.*;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.security.CustomUserDetails;
import com.example.photomory.service.OurAlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.photomory.exception.UnauthorizedException; // 기존에 있던 임포트
import jakarta.validation.Valid;
import java.lang.IllegalArgumentException;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/our-album")
public class OurAlbumController {

    private final OurAlbumService ourAlbumService;
    private final ObjectMapper objectMapper; // JSON 파싱을 위한 ObjectMapper 주입

    // 0. 기본 페이지 데이터 조회
    @GetMapping
    public ResponseEntity<List<OurAlbumResponseDefaultDto>> getAllUserGroupsDefaultData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 사용자 인증 확인
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity user = userDetails.getUser();
        List<OurAlbumResponseDefaultDto> userGroupsDetail = ourAlbumService.getAllGroupsDetailForUser(user.getUserId());
        return ResponseEntity.ok(userGroupsDetail);
    }

    // 1. 그룹 생성
    @PostMapping("/group")
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody @Valid GroupCreateRequestDto requestDto,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity user = userDetails.getUser();
        GroupResponseDto responseDto = ourAlbumService.createGroup(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 2. 그룹 정보 + 구성원 반환
    @GetMapping("/group/{groupId}")
    public ResponseEntity<GroupFullInfoResponseDto> getGroupFullInfo(@PathVariable Long groupId) {
        GroupFullInfoResponseDto responseDto = ourAlbumService.getGroupFullInfo(groupId);
        return ResponseEntity.ok(responseDto);
    }

    // 3. 앨범 생성
    @PostMapping("/group/{groupId}/album")
    public ResponseEntity<AlbumResponseDto> createAlbum(@PathVariable Long groupId,
                                                        @RequestBody @Valid AlbumCreateRequestDto requestDto,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity user = userDetails.getUser();
        AlbumResponseDto responseDto = ourAlbumService.createAlbum(groupId, requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 4. 앨범 상세정보 + 포스트 목록 (페이징 적용)
    @GetMapping("/album/{albumId}")
    public ResponseEntity<AlbumWithPostsResponseDto> getAlbumWithPosts(@PathVariable Long albumId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        AlbumWithPostsResponseDto responseDto = ourAlbumService.getAlbumWithPosts(albumId, page, size);
        return ResponseEntity.ok(responseDto);
    }

    // 5. 게시물 생성 (파일 포함)
    @PostMapping("/album/{albumId}/post")
    public ResponseEntity<PostResponseDto> createPost(@PathVariable Long albumId,
                                                      @RequestPart String requestDtoJson, // JSON 문자열로 DTO 받음
                                                      @RequestPart(name = "photo", required = false) MultipartFile photo, // 사진 파일 받음
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        // 사용자 인증 확인
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity user = userDetails.getUser();

        // requestDtoJson 파싱
        PostCreateRequestDto requestDto;
        try {
            requestDto = objectMapper.readValue(requestDtoJson, PostCreateRequestDto.class);
        } catch (Exception e) {
            // JSON 파싱 실패 시 예외 처리
            throw new IllegalArgumentException("Invalid PostCreateRequestDto JSON: " + e.getMessage(), e);
        }

        // 서비스 호출
        PostResponseDto responseDto = ourAlbumService.createPost(albumId, requestDto, photo, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 7. 댓글 작성
    @PostMapping("/{albumId}/post/{postId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Integer albumId,
            @PathVariable Integer postId,
            @RequestBody @Valid CommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity user = userDetails.getUser();

        // 경로 변수와 DTO 본문의 ID 일치 여부 확인
        if (!albumId.equals(requestDto.getAlbumId()) || !postId.equals(requestDto.getPostId())) {
            throw new IllegalArgumentException("요청 경로와 본문의 앨범 또는 게시글 ID가 일치하지 않습니다.");
        }

        CommentResponseDto response = ourAlbumService.createComment(albumId, postId, user, requestDto.getCommentsText());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 9. 초대 가능한 친구 목록 조회 (그룹 멤버 제외)
    @GetMapping("/group/{groupId}/invitable-friends")
    public ResponseEntity<List<UserSummaryDto>> getInvitableFriends(@PathVariable Long groupId,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity user = userDetails.getUser();
        List<UserSummaryDto> friends = ourAlbumService.getFriendsExcludingGroup(groupId, user.getUserId());
        return ResponseEntity.ok(friends);
    }

    // 10. 친구를 그룹에 초대
    @PostMapping("/group/{groupId}/invite")
    public ResponseEntity<String> inviteToGroup(@PathVariable Long groupId,
                                                @RequestBody List<Long> friendIds,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity inviter = userDetails.getUser();
        ourAlbumService.inviteToGroup(groupId, inviter, friendIds);
        return ResponseEntity.ok("친구 초대가 완료되었습니다.");
    }

    // 11. 친구 그룹에서 삭제
    @DeleteMapping("/group/{groupId}/member/{userIdToRemove}") // @DeleteMapping 어노테이션 수정
    public ResponseEntity<Void> removeMemberFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long userIdToRemove,
            @AuthenticationPrincipal CustomUserDetails userDetails // 삭제 권한 확인을 위해 userDetails 추가
    ) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity currentUser = userDetails.getUser();
        // 서비스 메서드에 currentUser를 전달하여 권한 확인 로직 추가
        ourAlbumService.removeMemberFromGroup(groupId, userIdToRemove, currentUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 12. 특정 앨범에서 게시글 삭제
    @DeleteMapping("/album/{albumId}/post/{postId}")
    public ResponseEntity<Void> deletePostInAlbum(
            @PathVariable Integer albumId,   // Integer 타입으로 변경
            @PathVariable Integer postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity currentUser = userDetails.getUser();
        ourAlbumService.deletePostWithFile(albumId, postId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // 13. 앨범 삭제
    @DeleteMapping("/album/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long albumId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 없습니다. 로그인해주세요.");
        }
        UserEntity currentUser = userDetails.getUser();
        ourAlbumService.deleteAlbum(albumId, currentUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content 반환
    }
}