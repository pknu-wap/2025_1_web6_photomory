package com.example.photomory.repository;

import com.example.photomory.entity.Tag;
import com.example.photomory.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByPost(Post post);
}
