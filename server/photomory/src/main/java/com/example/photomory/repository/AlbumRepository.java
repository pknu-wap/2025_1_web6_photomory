package com.example.photomory.repository;

import com.example.photomory.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;



@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("SELECT a FROM Album a " +
            "LEFT JOIN AlbumConnect ac ON a.albumId = ac.albumId " +
            "WHERE a.albumId = :albumId AND a.userId = :userId AND ac.albumId IS NULL")
    Optional<Album> findMyAlbum(@Param("albumId") Long albumId,
                                @Param("userId") Long userId);
}
