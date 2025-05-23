package com.example.photomory.repository;

import com.example.photomory.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    // 그룹ID로 앨범 목록 조회
    List<Album> findByMyAlbum_MyalbumId(Integer groupId);
}
