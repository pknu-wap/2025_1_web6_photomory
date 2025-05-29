package com.example.photomory.repository;

import com.example.photomory.entity.Photo;
import com.example.photomory.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByPost(Post post);
}
