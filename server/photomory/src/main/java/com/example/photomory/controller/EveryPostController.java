package com.example.photomory.controller;

import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.service.EveryPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/every/posts")
public class EveryPostController {

    private final EveryPostService everyPostService;

    @GetMapping
    public ResponseEntity<List<EveryPostResponseDto>> getAllPosts() {
        List<EveryPostResponseDto> posts = everyPostService.getAllPostsWithComments();
        return ResponseEntity.ok(posts);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createPost(
            @AuthenticationPrincipal UserEntity user,
            @RequestPart("postText") String postText,
            @RequestPart("postDescription") String postDescription,
            @RequestPart("location") String location,
            @RequestPart("photo") MultipartFile photo,
            @RequestPart("photoName") String photoName,
            @RequestPart("photoComment") String photoComment,
            @RequestPart("photoMakingTime") String photoMakingTime,
            @RequestPart("tags") String tagsJson // JSON 문자열로 받기
    ) {
        EveryPostRequestDto dto = new EveryPostRequestDto();
        dto.setPostText(postText);
        dto.setPostDescription(postDescription);
        dto.setLocation(location);
        dto.setPhoto(photo);
        dto.setPhotoName(photoName);
        dto.setPhotoComment(photoComment);
        dto.setPhotoMakingTime(photoMakingTime);
        dto.setTagsJson(tagsJson); // setter를 따로 구현해야 함

        everyPostService.createPost(dto, user.getUserEmail());
        return ResponseEntity.ok("게시글이 성공적으로 등록되었습니다.");
    }
}
