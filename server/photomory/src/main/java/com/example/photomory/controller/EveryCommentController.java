package com.example.photomory.controller;

import com.example.photomory.dto.EveryCommentRequestDto;
import com.example.photomory.service.EveryCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/every")
@RequiredArgsConstructor
public class EveryCommentController {

    private final EveryCommentService everyCommentService;

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody EveryCommentRequestDto dto,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            System.out.println("❌ 인증되지 않은 사용자입니다.");
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String userEmail = userDetails.getUsername();
        System.out.println("✅ 인증된 사용자: " + userEmail);

        everyCommentService.addComment(dto, userEmail);
        return ResponseEntity.ok("댓글 작성 완료");
    }
}
