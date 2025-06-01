package com.example.photomory.repository;

import com.example.photomory.entity.OurAlbum; // OurAlbum 엔티티 임포트
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // @Query 임포트 추가
import org.springframework.data.repository.query.Param; // @Param 임포트 추가
import org.springframework.stereotype.Repository;

import java.util.List; // List 반환 타입을 위해 임포트

@Repository
public interface OurAlbumRepository extends JpaRepository<OurAlbum, Integer> {
    // JpaRepository의 두 번째 인자는 OurAlbum의 PK 타입인 Integer 입니다.

    /**
     *
     * @param albumId 조회할 OurAlbum의 ID
     * @return 해당 ID의 OurAlbum 리스트 (게시물 및 사진이 페치 조인됨)
     */
    @Query("SELECT DISTINCT oa FROM OurAlbum oa " +
            "LEFT JOIN FETCH oa.posts op " +       // OurAlbum과 OurPost를 페치 조인 (oa.posts는 OurAlbum 엔티티의 posts 필드명)
            "LEFT JOIN FETCH op.photos " +         // OurPost와 Photo를 페치 조인 (op.photos는 OurPost 엔티티의 photos 필드명)
            "WHERE oa.albumId = :albumId")
    List<OurAlbum> findAllByAlbumIdWithPostsAndPhotos(@Param("albumId") Integer albumId); // <--- 이 메서드를 추가합니다.

}