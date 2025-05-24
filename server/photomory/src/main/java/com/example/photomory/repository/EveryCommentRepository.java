package com.example.photomory.repository;

import com.example.photomory.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EveryCommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostId(Integer postId);
}
