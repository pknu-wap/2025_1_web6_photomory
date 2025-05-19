package com.example.photomory.controller;

import com.example.photomory.dto.CommentRequestDto;
import com.example.photomory.service.EveryCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/every")
@RequiredArgsConstructor
public class EveryCommentController {

    private final EveryCommentService everyCommentService;

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody CommentRequestDto dto) {
        everyCommentService.addComment(dto);
        return ResponseEntity.ok("댓글 작성 완료");
    }
}
