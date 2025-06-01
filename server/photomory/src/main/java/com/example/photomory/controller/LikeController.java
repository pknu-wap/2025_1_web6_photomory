package com.example.photomory.controller;

import com.example.photomory.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts") // 좋아요 기능이 모든 게시글 타입에 적용되므로, 더 일반적인 경로로 변경합니다.
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     *
     * @param postType 좋아요를 누를 게시글의 타입 ("OUR", "MY", "EVERY")
     * @param postId 좋아요를 누르거나 취소할 게시글의 ID
     * @param userDetails 현재 인증된 사용자의 정보
     * @return 성공 메시지
     */
    @PostMapping("/{postType}/{postId}/like") // postType과 postId를 경로 변수로 받도록 수정합니다.
    public ResponseEntity<String> toggleLike(@PathVariable String postType,
                                             @PathVariable Long postId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        // LikeService의 toggleLike 메서드에 postType을 세 번째 인자로 전달합니다.
        likeService.toggleLike(postId, userDetails.getUsername(), postType);
        return ResponseEntity.ok("좋아요 토글 완료");
    }

    /**
     *
     * @param postType 좋아요 개수를 조회할 게시글의 타입 ("OUR", "MY", "EVERY")
     * @param postId 좋아요 개수를 조회할 게시글의 ID
     * @return 해당 게시글의 좋아요 개수
     */
    @GetMapping("/{postType}/{postId}/like/count") // postType과 postId를 경로 변수로 받도록 수정합니다.
    public ResponseEntity<Long> getLikeCount(@PathVariable String postType,
                                             @PathVariable Long postId) {
        // LikeService의 getLikeCount 메서드에 postType을 두 번째 인자로 전달합니다.
        Long likeCount = likeService.getLikeCount(postId, postType);
        return ResponseEntity.ok(likeCount);
    }
}