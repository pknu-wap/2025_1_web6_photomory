package com.example.photomory.repository;

import com.example.photomory.entity.MyAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyAlbumRepository extends JpaRepository<MyAlbum, Long> {
    
    List<MyAlbum> findByUserId(Long userId);
}
