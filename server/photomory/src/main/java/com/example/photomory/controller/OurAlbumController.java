package com.example.photomory.controller;

import com.example.photomory.dto.CommentRequestDto;
import com.example.photomory.dto.CommentResponseDto;
import com.example.photomory.dto.OurAlbumResponseDto;
import com.example.photomory.entity.Album;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.service.OurAlbumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/albums")
public class OurAlbumController {

    private final OurAlbumService ourAlbumService;
    private final CommentRepository commentRepository;

    public OurAlbumController(OurAlbumService ourAlbumService,
                              CommentRepository commentRepository) {
        this.ourAlbumService = ourAlbumService;
        this.commentRepository = commentRepository;
    }

    // 앨범 상세 조회
    @GetMapping("/{albumId}")
    public ResponseEntity<?> getAlbumDetail(
            @PathVariable Integer albumId,
            Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof UserEntity)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long userId = user.getUserId();

        try {
            OurAlbumResponseDto response = ourAlbumService.getAlbumDetails(albumId, userId);
            return ResponseEntity.ok(response);
        } catch (SecurityException se) {
            return ResponseEntity.status(403).body(se.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }

    // 댓글 작성
    @PostMapping("/{albumId}/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Integer albumId,
                                           @PathVariable Integer postId,
                                           @RequestBody CommentRequestDto requestDto,
                                           Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof UserEntity)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        UserEntity user = (UserEntity) authentication.getPrincipal();

        try {
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setCommentsText(requestDto.getCommentsText());

            // user 엔티티 직접 설정
            comment.setUser(user);

            // album 엔티티 ID만 세팅 (필요시 albumService 통해 조회 가능)
            Album album = new Album();
            album.setAlbumId(albumId);
            comment.setAlbum(album);

            Comment saved = commentRepository.save(comment);
            return ResponseEntity.ok(CommentResponseDto.fromEntity(saved));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("댓글 저장 중 오류가 발생했습니다.");
        }
    }

    // 댓글 조회
    @GetMapping("/{albumId}/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable Integer albumId,
                                         @PathVariable Integer postId,
                                         Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof UserEntity)) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        try {
            List<Comment> comments = commentRepository.findByAlbumIdAndPostId(albumId, postId);
            List<CommentResponseDto> response = comments.stream()
                    .map(CommentResponseDto::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("댓글 조회 중 오류가 발생했습니다.");
        }
    }
}
