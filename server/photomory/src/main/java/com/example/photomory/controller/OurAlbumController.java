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

    // 2. 그룹 정보 조회
    @GetMapping("/group/{groupId}")
    public GroupInfoResponseDto getGroupInfo(@PathVariable Long groupId) {
        return ourAlbumService.getGroupInfo(groupId);
    }

    // 3. 앨범 생성
    @PostMapping("/group/{groupId}/album")
    public AlbumResponseDto createAlbum(@PathVariable Long groupId,
                                        @RequestBody AlbumCreateRequestDto requestDto,
                                        @AuthenticationPrincipal UserEntity user) {
        return ourAlbumService.createAlbum(groupId, requestDto, user);
    }

    // 4. 앨범 게시글 목록 조회 (페이징)
    @GetMapping("/album/{albumId}/posts")
    public List<PostResponseDto> getAlbumPosts(@PathVariable Integer albumId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return ourAlbumService.getAlbumPosts(albumId, page, size);
    }

    // 5. 게시글 생성 (파일 포함)
    @PostMapping("/album/{albumId}/post")
    public PostResponseDto createPost(@PathVariable Integer albumId,
                                      @RequestPart PostCreateRequestDto requestDto,
                                      @RequestPart(required = false) MultipartFile photo,
                                      @AuthenticationPrincipal UserEntity user) throws IOException {
        return ourAlbumService.createPost(albumId, requestDto, photo, user);
    }

    // 6. 앨범 전체 조회 (포스트 + 댓글 포함)
    @GetMapping("/album/{albumId}/detail")
    public OurAlbumFullResponseDto getAlbumFullDetails(@PathVariable Integer albumId,
                                                       @AuthenticationPrincipal UserEntity user) {
        return ourAlbumService.getAlbumFullDetails(albumId, user.getUserId());
    }

    // 7. 게시글 상세 조회
    @GetMapping("/post/{postId}")
    public PostDetailResponseDto getPostDetail(@PathVariable Long postId) {
        return ourAlbumService.getPostDetail(postId);
    }

    // 8. 댓글 생성
    @PostMapping("/album/{albumId}/post/{postId}/comment")
    public CommentResponseDto createComment(@PathVariable Integer albumId,
                                            @PathVariable Integer postId,
                                            @RequestBody String text,
                                            @AuthenticationPrincipal UserEntity user) {
        return ourAlbumService.createComment(albumId, postId, user, text);
    }

    // 9. 달력 태그 조회
    @GetMapping("/group/{groupId}/calendar-tags")
    public List<CalendarTagResponseDto> getCalendarTags(@PathVariable Long groupId) {
        return ourAlbumService.getCalendarTags(groupId);
    }

    // 10. 그룹 멤버 조회
    @GetMapping("/group/{groupId}/members")
    public List<UserSummaryDto> getGroupMembers(@PathVariable Long groupId) {
        return ourAlbumService.getGroupMembers(groupId);
    }

    // 11. 초대 가능한 친구 목록 조회
    @GetMapping("/group/{groupId}/invitable-friends")
    public List<UserSummaryDto> getInvitableFriends(@PathVariable Long groupId,
                                                    @AuthenticationPrincipal UserEntity user) {
        return ourAlbumService.getFriendsExcludingGroup(groupId, user.getUserId());
    }

    // 12. 친구를 그룹에 초대
    @PostMapping("/group/{groupId}/invite")
    public void inviteToGroup(@PathVariable Long groupId,
                              @RequestBody List<Long> friendIds,
                              @AuthenticationPrincipal UserEntity inviter) {
        ourAlbumService.inviteToGroup(groupId, inviter, friendIds);
    }
}
