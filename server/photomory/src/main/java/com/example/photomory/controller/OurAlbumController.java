package com.example.photomory.controller;

import com.example.photomory.dto.*;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.service.OurAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/our-album")
public class OurAlbumController {

    private final OurAlbumService ourAlbumService;

    // 1. 그룹 생성
    @PostMapping("/group")
    public MyAlbumResponseDto createGroup(@RequestBody GroupCreateRequestDto requestDto,
                                          @AuthenticationPrincipal UserEntity user) {
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
                                        @AuthenticationPrincipal UserEntity user) {
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
    public PostResponseDto createPost(@PathVariable Long albumId, // Integer -> Long
                                      @RequestPart PostCreateRequestDto requestDto,
                                      @RequestPart(required = false) MultipartFile photo,
                                      @AuthenticationPrincipal UserEntity user) throws IOException {
        return ourAlbumService.createPost(albumId, requestDto, photo, user);
    }

    // 6. 게시물 클릭 시 상세 보기 (사진 확대, 댓글, 좋아요 수)
    @GetMapping("/post/{postId}/detail")
    public PostZoomDetailResponseDto getPostZoomDetail(@PathVariable Long postId) { // Integer -> Long
        return ourAlbumService.getPostZoomDetail(postId);
    }

    // 7. 댓글 작성
    @PostMapping("/album/{albumId}/post/{postId}/comment")
    public CommentResponseDto createComment(@PathVariable Long albumId, // Integer -> Long
                                            @PathVariable Long postId, // Integer -> Long
                                            @RequestBody String text,
                                            @AuthenticationPrincipal UserEntity user) {
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
                                                    @AuthenticationPrincipal UserEntity user) {
        return ourAlbumService.getFriendsExcludingGroup(groupId, user.getUserId());
    }

    // 10. 친구를 그룹에 초대
    @PostMapping("/group/{groupId}/invite")
    public String inviteToGroup(@PathVariable Long groupId,
                                @RequestBody List<Long> friendIds,
                                @AuthenticationPrincipal UserEntity inviter) {
        ourAlbumService.inviteToGroup(groupId, inviter, friendIds);
        return "친구 초대가 완료되었습니다."; // void 타입이므로 성공 메시지 반환
    }
}