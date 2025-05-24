package com.example.photomory.controller;

import com.example.photomory.dto.CommentRequestDto;
import com.example.photomory.dto.EveryCommentDto;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.EveryCommentRepository;
import com.example.photomory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/every/comments")
public class EveryCommentController {

    @Autowired
    private EveryCommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{postId}")
    public List<EveryCommentDto> getCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> {
            UserEntity user = userRepository.findById(comment.getUserId().longValue()).orElse(null);
            return EveryCommentDto.from(comment, user != null ? user.getUserName() : "알 수 없음");
        }).collect(Collectors.toList());
    }

    @PostMapping
    public void addComment(@RequestBody CommentRequestDto dto) {
        Comment comment = new Comment();
        comment.setPostId(dto.getPostId());
        comment.setUserId(dto.getUserId());
        comment.setAlbumId(dto.getAlbumId());
        comment.setCommentsText(dto.getCommentsText());
        comment.setCommentCount(dto.getCommentCount());
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }
}
