package com.example.photomory.service;

import com.example.photomory.entity.Comment;
import com.example.photomory.repository.EveryCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EveryCommentService {

    private final EveryCommentRepository commentRepository;

    public EveryCommentService(EveryCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPostId(postId);
    }
}
