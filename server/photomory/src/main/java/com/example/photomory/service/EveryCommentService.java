package com.example.photomory.service;

import com.example.photomory.entity.Comment;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EveryCommentService {

    private final CommentRepository commentRepository;

    public EveryCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPost_PostId(postId);
    }
}
