package com.example.photomory.repository;

import com.example.photomory.entity.MyPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyPostRepository extends JpaRepository<MyPost, Integer> {
}
