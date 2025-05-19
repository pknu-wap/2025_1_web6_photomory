package com.example.photomory.repository;

import com.example.photomory.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    void deleteByImageUrl(String imageUrl);

    //2차-날짜랑 제목추가
    List<ImageEntity> findByTitleAndDate(String title, String date);
}
