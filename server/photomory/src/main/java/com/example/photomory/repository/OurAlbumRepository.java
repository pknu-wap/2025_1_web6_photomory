package com.example.photomory.repository;

import com.example.photomory.entity.OurAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OurAlbumRepository extends JpaRepository<OurAlbum, Integer> {

    /**
     * @param groupId 그룹 ID (UserGroup.id)
     * @return 해당 그룹에 속한 앨범 리스트
     */
    @Query("SELECT DISTINCT oa FROM OurAlbum oa " +
            "LEFT JOIN FETCH oa.posts op " +
            "LEFT JOIN FETCH op.photos " +
            "WHERE oa.userGroup.id = :groupId")
    List<OurAlbum> findAllByGroupIdWithPostsAndPhotos(@Param("groupId") Long groupId);
}
