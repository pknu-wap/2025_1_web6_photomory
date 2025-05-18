package com.example.photomory.repository;

import com.example.photomory.entity.MyAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyAlbumRepository extends JpaRepository<MyAlbum, Long> {

    Optional<MyAlbum> findByUserId(Long userId);
}
