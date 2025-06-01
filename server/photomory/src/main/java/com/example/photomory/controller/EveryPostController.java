package com.example.photomory.controller;

import com.example.photomory.dto.EveryPostRequestDto;
import com.example.photomory.dto.EveryPostResponseDto;
import com.example.photomory.dto.EveryPostUpdateDto;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.service.EveryPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public ResponseEntity<List<EveryPostResponseDto>> getAllPosts(@AuthenticationPrincipal(expression = "user") UserEntity user) {
        List<EveryPostResponseDto> posts = everyPostService.getAllPostsWithComments(user.getUserEmail());
        return ResponseEntity.ok(posts);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EveryPostResponseDto> createPost(
            @AuthenticationPrincipal(expression = "user") UserEntity user,
            @RequestPart("postText") String postText,
            @RequestPart("postDescription") String postDescription,
            @RequestPart("location") String location,
            @RequestPart("photo") MultipartFile photo,
            @RequestPart("photoMakingTime") String photoMakingTime,
            @RequestPart("tagsJson") String tagsJson
    ) {
        EveryPostRequestDto dto = new EveryPostRequestDto();
        dto.setPostText(postText);
        dto.setPostDescription(postDescription);
        dto.setLocation(location);
        dto.setPhoto(photo);
        dto.setPhotoMakingTime(photoMakingTime);
        dto.setTagsJson(tagsJson);
        dto.parseTags();

        EveryPostResponseDto response = everyPostService.createPost(dto, user.getUserEmail());
        return ResponseEntity.ok(response);
    }


    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePost(
            @PathVariable Integer postId,
            @AuthenticationPrincipal(expression = "user") UserEntity user,
            @RequestPart("postText") String postText,
            @RequestPart("postDescription") String postDescription,
            @RequestPart("location") String location,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart("photoMakingTime") String photoMakingTime,
            @RequestPart("tagsJson") String tagsJson
    ) {
        EveryPostUpdateDto dto = new EveryPostUpdateDto();
        dto.setPostText(postText);
        dto.setPostDescription(postDescription);
        dto.setLocation(location);
        dto.setPhoto(photo);
        dto.setPhotoMakingTime(photoMakingTime);
        dto.setTagsJson(tagsJson);
        dto.parseTags();

        everyPostService.updatePost(postId, dto, user.getUserEmail());
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }
}
