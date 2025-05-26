package com.example.photomory.service;

import com.example.photomory.dto.EveryCommentRequestDto;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.PostRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EveryCommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void addComment(EveryCommentRequestDto dto, String userEmail) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저 못 찾음"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글 못 찾음"));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .commentText(dto.getCommentsText())
                .commentTime(LocalDateTime.now())
                .build();
        commentRepository.save(comment);

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);
    }

}
