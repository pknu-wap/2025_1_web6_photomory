package com.example.photomory.controller;

import com.example.photomory.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/every/posts")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        likeService.toggleLike(postId, userDetails.getUsername());
        return ResponseEntity.ok("좋아요 토글 완료");
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikeCount(postId));
    }
}
