package com.example.photomory.controller;

import com.example.photomory.dto.*;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.service.OurAlbumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OurAlbumController {

    private final OurAlbumService ourAlbumService;

    public OurAlbumController(OurAlbumService ourAlbumService) {
        this.ourAlbumService = ourAlbumService;
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof UserEntity;
    }

    private UserEntity getAuthenticatedUser(Authentication authentication) {
        return (UserEntity) authentication.getPrincipal();
    }

    // 1. 그룹 생성
    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(
            @RequestBody MyAlbumCreateRequestDto requestDto,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = getAuthenticatedUser(authentication);
        MyAlbumResponseDto response = ourAlbumService.createGroup(requestDto, user);
        return ResponseEntity.ok(response);
    }

    // 2. 앨범 생성
    @PostMapping("/groups/{groupId}/albums")
    public ResponseEntity<?> createAlbum(
            @PathVariable Long groupId,
            @RequestBody AlbumCreateRequestDto requestDto,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = getAuthenticatedUser(authentication);
        try {
            AlbumResponseDto response = ourAlbumService.createAlbum(groupId, requestDto, user);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 3. 게시물 생성
    @PostMapping("/albums/{albumId}/posts")
    public ResponseEntity<?> createPost(
            @PathVariable Integer albumId,
            @RequestBody PostCreateRequestDto requestDto,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = getAuthenticatedUser(authentication);

        try {
            PostResponseDto response = ourAlbumService.createPost(albumId, requestDto, user);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("게시물 생성 중 오류가 발생했습니다.");
        }
    }

    // 4. 앨범 전체 조회 (상세 + 게시물 + 댓글 포함)
    @GetMapping("/albums/{albumId}")
    public ResponseEntity<?> getAlbumFullDetails(
            @PathVariable Integer albumId,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = getAuthenticatedUser(authentication);
        Long userId = user.getUserId();

        try {
            OurAlbumFullResponseDto response = ourAlbumService.getAlbumFullDetails(albumId, userId);
            return ResponseEntity.ok(response);
        } catch (SecurityException se) {
            return ResponseEntity.status(403).body(se.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

    // 5. 댓글 작성
    @PostMapping("/albums/{albumId}/comments")
    public ResponseEntity<?> createComment(
            @PathVariable Integer albumId,
            @RequestBody CommentCreateRequestDto requestDto,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = getAuthenticatedUser(authentication);

        try {
            CommentResponseDto savedComment = ourAlbumService.createComment(
                    albumId,
                    requestDto.getPostId(),
                    user,
                    requestDto.getCommentsText()
            );
            return ResponseEntity.ok(savedComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("댓글 저장 중 오류가 발생했습니다.");
        }
    }
}
