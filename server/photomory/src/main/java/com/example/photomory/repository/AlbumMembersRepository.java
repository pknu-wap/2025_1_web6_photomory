package com.example.photomory.repository;

import com.example.photomory.entity.AlbumMembers;
import com.example.photomory.entity.AlbumMembers.AlbumMembersId;
import com.example.photomory.entity.MyAlbum;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumMembersRepository extends JpaRepository<AlbumMembers, AlbumMembersId> {

    boolean existsByUserEntityAndMyAlbum(UserEntity userEntity, MyAlbum myAlbum);

    List<AlbumMembers> findByMyAlbum_MyalbumId(Integer myalbumId);

    List<AlbumMembers> findByUserEntity_UserId(Long userId);

    Optional<AlbumMembers> findByMyAlbum_MyalbumIdAndUserEntity_UserId(Integer myalbumId, Long userId);

    long countByMyAlbum_MyalbumId(Integer myalbumId);
}