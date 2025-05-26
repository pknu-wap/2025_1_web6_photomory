package com.example.photomory.controller;

import com.example.photomory.dto.CommentRequestDto;
import com.example.photomory.dto.EveryCommentDto;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.Album;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.UserRepository;
import com.example.photomory.repository.PostRepository;
import com.example.photomory.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.security.CustomUserDetails;
import com.example.photomory.service.EveryCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/every/comments")
public class EveryCommentController {

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping("/{postId}")
    public List<EveryCommentDto> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentRepository.findByPost_PostId(postId);
        return comments.stream().map(comment -> {
            UserEntity user = comment.getUser();
            String userName = (user != null) ? user.getUserName() : "알 수 없음";
            return EveryCommentDto.from(comment, userName);
        }).collect(Collectors.toList());
    }

    @PostMapping
    public void addComment(@RequestBody CommentRequestDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("유저 없음"));
        Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new RuntimeException("게시글 없음"));
        Album album = albumRepository.findById(dto.getAlbumId()).orElse(null);

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .album(album)
                .commentsText(dto.getCommentsText())
                .commentTime(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }
}
