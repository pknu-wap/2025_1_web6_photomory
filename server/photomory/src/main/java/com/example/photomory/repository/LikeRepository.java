package com.example.photomory.repository;

import com.example.photomory.entity.Like;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(UserEntity user, Post post);
    Long countByPost(Post post);
    void deleteByUserAndPost(UserEntity user, Post post);
}
