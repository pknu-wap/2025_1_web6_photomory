package com.example.photomory.repository;

import com.example.photomory.entity.AlbumMembers;
import com.example.photomory.entity.AlbumMembers.AlbumMembersId;
import com.example.photomory.entity.OurAlbum; // MyAlbum 대신 OurAlbum 임포트
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumMembersRepository extends JpaRepository<AlbumMembers, AlbumMembersId> {

    boolean existsByUserEntityAndOurAlbum(UserEntity userEntity, OurAlbum ourAlbum);

    List<AlbumMembers> findByOurAlbum_AlbumId(Integer albumId);

    List<AlbumMembers> findByUserEntity_UserId(Long userId);

    Optional<AlbumMembers> findByOurAlbum_AlbumIdAndUserEntity_UserId(Integer albumId, Long userId);

    long countByOurAlbum_AlbumId(Integer albumId);

    @Query("SELECT am.userEntity.userId FROM AlbumMembers am WHERE am.ourAlbum.albumId = :albumId")
    List<Long> findUserIdsByOurAlbumId(@Param("albumId") Integer albumId); // Long 대신 Integer로 변경 (OurAlbum의 ID 타입에 맞춤)
}