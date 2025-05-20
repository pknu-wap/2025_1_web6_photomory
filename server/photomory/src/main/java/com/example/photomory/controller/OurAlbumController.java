package com.example.photomory.controller;

import com.example.photomory.dto.*;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.service.OurAlbumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/albums")
public class OurAlbumController {

    private final OurAlbumService ourAlbumService;

    public OurAlbumController(OurAlbumService ourAlbumService) {
        this.ourAlbumService = ourAlbumService;
    }

    // 1. 앨범 전체 조회 (상세 + 게시물 + 댓글)
    @GetMapping("/{albumId}")
    public ResponseEntity<?> getAlbumFullDetails(
            @PathVariable Integer albumId,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = (UserEntity) authentication.getPrincipal();
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

    // 2. 댓글 작성
    @PostMapping("/{albumId}")
    public ResponseEntity<?> createComment(
            @PathVariable Integer albumId,
            @RequestBody CommentCreateRequestDto requestDto,
            Authentication authentication) {

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = (UserEntity) authentication.getPrincipal();

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

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof UserEntity;
    }
}
