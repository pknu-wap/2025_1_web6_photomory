package com.example.photomory.repository;

import com.example.photomory.entity.MyAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyAlbumRepository extends JpaRepository<MyAlbum, Long> {
}
