package com.example.photomory.controller;

import com.example.photomory.dto.*;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.security.CustomUserDetails;
import com.example.photomory.service.OurAlbumService;
import com.fasterxml.jackson.databind.ObjectMapper; // <--- 이 임포트 추가
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/our-album")
public class OurAlbumController {

    private final OurAlbumService ourAlbumService;
    private final ObjectMapper objectMapper;

    // 0. 기본 우리의 추억에서 보내줄 데이터
    @GetMapping("")
    public OurAlbumResponseDefaultDto getOurAlbum(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = userDetails.getUser();
        return ourAlbumService.getOurAlbumData(user.getUserId());
    }

    // 1. 그룹 생성
    @PostMapping("/group")
    public GroupResponseDto createGroup(@RequestBody GroupCreateRequestDto requestDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = userDetails.getUser();
        return ourAlbumService.createGroup(requestDto, user);
    }

    // 2. 그룹 정보 + 구성원 반환
    @GetMapping("/group/{groupId}")
    public GroupFullInfoResponseDto getGroupFullInfo(@PathVariable Long groupId) {
        return ourAlbumService.getGroupFullInfo(groupId);
    }

    // 3. 앨범 생성
    @PostMapping("/group/{groupId}/album")
    public AlbumResponseDto createAlbum(@PathVariable Long groupId,
                                        @RequestBody AlbumCreateRequestDto requestDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = userDetails.getUser();
        return ourAlbumService.createAlbum(groupId, requestDto, user);
    }

    // 4. 앨범 상세정보 + 포스트 목록 (페이징 적용)
    @GetMapping("/album/{albumId}")
    public AlbumWithPostsResponseDto getAlbumWithPosts(@PathVariable Long albumId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return ourAlbumService.getAlbumWithPosts(albumId, page, size);
    }

    // 5. 게시물 생성 (파일 포함)
    @PostMapping("/album/{albumId}/post")
    public PostResponseDto createPost(@PathVariable Long albumId,
                                      @RequestPart String requestDtoJson, // DTO를 JSON 문자열로 받습니다.
                                      @RequestPart(required = false) MultipartFile photo, // 파일은 그대로 MultipartFile로 받습니다.
                                      @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        UserEntity user = userDetails.getUser();

        // JSON 문자열을 PostCreateRequestDto 객체로 변환
        PostCreateRequestDto requestDto = null;
        try {
            requestDto = objectMapper.readValue(requestDtoJson, PostCreateRequestDto.class);
        } catch (Exception e) {
            // JSON 파싱 실패 시 처리 (예: BadRequest 예외를 던져 클라이언트에게 알려줌)
            // 실제 운영 환경에서는 더 구체적인 예외 처리 로직이 필요합니다.
            throw new IllegalArgumentException("Invalid PostCreateRequestDto JSON: " + e.getMessage(), e);
        }

        return ourAlbumService.createPost(albumId, requestDto, photo, user);
    }

    // 6. 게시물 클릭 시 상세 보기 (사진 확대, 댓글, 좋아요 수)
    @GetMapping("/post/{postId}/detail")
    public PostZoomDetailResponseDto getPostZoomDetail(@PathVariable Long postId) {
        return ourAlbumService.getPostZoomDetail(postId);
    }

    // 7. 댓글 작성
    @PostMapping("/album/{albumId}/post/{postId}/comment")
    public CommentResponseDto createComment(@PathVariable Long albumId,
                                            @PathVariable Long postId,
                                            @RequestBody String text,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = userDetails.getUser();
        return ourAlbumService.createComment(albumId, postId, user, text);
    }

    // 8. 달력 태그 조회
    @GetMapping("/group/{groupId}/calendar-tags")
    public List<CalendarTagResponseDto> getCalendarTags(@PathVariable Long groupId) {
        return ourAlbumService.getCalendarTags(groupId);
    }

    // 9. 초대 가능한 친구 목록 조회 (그룹 멤버 제외)
    @GetMapping("/group/{groupId}/invitable-friends")
    public List<UserSummaryDto> getInvitableFriends(@PathVariable Long groupId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = userDetails.getUser();
        return ourAlbumService.getFriendsExcludingGroup(groupId, user.getUserId());
    }

    // 10. 친구를 그룹에 초대
    @PostMapping("/group/{groupId}/invite")
    public String inviteToGroup(@PathVariable Long groupId,
                                @RequestBody List<Long> friendIds,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity inviter = userDetails.getUser();
        ourAlbumService.inviteToGroup(groupId, inviter, friendIds);
        return "친구 초대가 완료되었습니다.";
    }

    @DeleteMapping("/{groupId}/member/{userIdToRemove}")
    public ResponseEntity<Void> removeMemberFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long userIdToRemove
    ) {
        ourAlbumService.removeMemberFromGroup(groupId, userIdToRemove);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 성공 시 204 No Content 반환
    }
}