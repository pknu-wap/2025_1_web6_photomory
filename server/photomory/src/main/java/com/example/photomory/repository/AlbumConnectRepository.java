package com.example.photomory.repository;

import com.example.photomory.entity.AlbumConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AlbumConnectRepository extends JpaRepository<AlbumConnect, Long> {

    @Query("SELECT ac.albumId FROM AlbumConnect ac WHERE ac.userId = :groupId")
    List<Long> findAlbumIdsByGroupId(@Param("groupId") Long groupId);
}
