package com.example.photomory.repository;

import com.example.photomory.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    void deleteByImageUrl(String imageUrl);
}
