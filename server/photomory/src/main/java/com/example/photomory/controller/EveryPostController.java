package com.example.photomory.controller;

import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.service.EveryPostService;
import com.example.photomory.dto.EveryPostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/every")
@RequiredArgsConstructor
public class EveryPostController {

    private final EveryPostService everyPostService;

    @GetMapping("/posts")
    public ResponseEntity<List<EveryPostResponseDto>> getAllPostsWithComments() {
        List<EveryPostResponseDto> posts = everyPostService.getAllPostsWithComments();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody EveryPostRequestDto dto) {
        everyPostService.createPost(dto);
        return ResponseEntity.ok("게시글 작성 완료");
    }

}
