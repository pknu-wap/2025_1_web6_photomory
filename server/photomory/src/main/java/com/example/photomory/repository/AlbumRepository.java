package com.example.photomory.repository;

import com.example.photomory.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> { // Album ID가 Integer임을 확인
    // 필수 추가: MyAlbum ID (그룹 ID)로 해당 그룹에 속한 모든 앨범을 조회하는 메소드
    List<Album> findByMyAlbum_MyalbumId(Integer myAlbumId);
}