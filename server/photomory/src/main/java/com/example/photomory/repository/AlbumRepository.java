package com.example.photomory.repository;

import com.example.photomory.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    // 기본 CRUD 메서드가 이미 JpaRepository에 있음
}
