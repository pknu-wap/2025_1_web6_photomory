package com.example.photomory.controller;

import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.service.EveryPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/every")
@RequiredArgsConstructor
public class EveryPostController {

    private final EveryPostService everyPostService;

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody EveryPostRequestDto dto) {
        Long newPostId = everyPostService.createPost(dto);
        return ResponseEntity.ok("게시글이 등록되었습니다. post_id = " + newPostId);
    }
}
