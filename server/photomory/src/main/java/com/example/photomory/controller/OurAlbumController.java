package com.example.photomory.controller;

import com.example.photomory.dto.OurAlbumResponseDto;
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

    @GetMapping("/{albumId}")
    public ResponseEntity<?> getAlbumDetail(
            @PathVariable Integer albumId,
            Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof UserEntity)) {
            // 인증 안된 경우 401 Unauthorized
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = (UserEntity) authentication.getPrincipal();
        Integer userId = user.getUserId();

        try {
            OurAlbumResponseDto response = ourAlbumService.getAlbumDetails(albumId, userId);
            return ResponseEntity.ok(response);
        } catch (SecurityException se) {
            // 권한 없는 경우 403 Forbidden + 메시지 반환
            return ResponseEntity.status(403).body(se.getMessage());
        } catch (EntityNotFoundException e) {
            // 앨범이 없으면 404 Not Found + 메시지 반환
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            // 기타 서버 오류
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }
}
