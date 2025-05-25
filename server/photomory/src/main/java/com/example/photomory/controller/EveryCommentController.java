package com.example.photomory.controller;

import com.example.photomory.dto.CommentRequestDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.security.CustomUserDetails;
import com.example.photomory.service.EveryCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/every")
@RequiredArgsConstructor
public class EveryCommentController {

    private final EveryCommentService everyCommentService;

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody @Valid CommentRequestDto dto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = userDetails.getUser();

        everyCommentService.addComment(dto.getAlbumId(), dto.getPostId(), user, dto.getCommentsText());

        return ResponseEntity.ok("댓글 작성 완료");
    }
}
