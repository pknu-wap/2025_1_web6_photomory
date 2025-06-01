package com.example.photomory.repository;

import com.example.photomory.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ImageRepository extends JpaRepository<Photo, Long> {
    void deleteByPhotoUrl(String photoUrl);

    //2차-날짜랑 제목추가
    List<Photo> findByTitleAndDate(String title, LocalDate date);

    List<Photo> findByDate(LocalDate date);
}
