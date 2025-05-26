package com.example.photomory.controller;

import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.service.EveryPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<?> createPost(@RequestBody EveryPostRequestDto dto,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        everyPostService.createPost(dto, userDetails.getUsername());
        return ResponseEntity.ok("게시글 작성 완료");
    }
}
