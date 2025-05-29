package com.example.photomory.repository;

import com.example.photomory.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {

    List<Album> findByMyAlbum_MyalbumId(Integer myAlbumId);

    // 그룹 ID(MyAlbum ID)로 앨범과 연관된 posts를 함께 조회하는 fetch join 쿼리
    @Query("SELECT DISTINCT a FROM Album a " +
            "LEFT JOIN FETCH a.posts p " +
            "LEFT JOIN FETCH p.photos " +
            "WHERE a.myAlbum.myalbumId = :myAlbumId")
    List<Album> findAllByMyAlbumIdWithPostsAndPhotos(@Param("myAlbumId") Integer myAlbumId);
}
