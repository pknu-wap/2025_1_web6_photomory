package com.example.photomory.controller;

import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.service.EveryPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/every-posts")
@RequiredArgsConstructor
public class EveryPostController {

    private final EveryPostService everyPostService;

    @GetMapping
    public ResponseEntity<List<EveryPostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(everyPostService.getAllPosts());
    }
}