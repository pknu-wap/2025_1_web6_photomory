package com.example.photomory.repository;

import com.example.photomory.entity.AlbumMembers;
import com.example.photomory.entity.AlbumMembers.AlbumMembersId;
import com.example.photomory.entity.MyAlbum;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumMembersRepository extends JpaRepository<AlbumMembers, AlbumMembersId> {

    // 특정 유저가 특정 앨범에 속해 있는지 여부 확인
    boolean existsByUserEntityAndMyAlbum(UserEntity userEntity, MyAlbum myAlbum);

    // 특정 앨범에 속한 모든 멤버 조회 (Integer 사용) +hw
    List<AlbumMembers> findByMyAlbum_MyalbumId(Integer myalbumId);

    // 특정 유저가 속한 모든 앨범 조회 (User ID는 Long)
    List<AlbumMembers> findByUserEntity_UserId(Long userId);

    // 특정 유저가 특정 앨범에 속해 있는지 조회
    Optional<AlbumMembers> findByMyAlbum_MyalbumIdAndUserEntity_UserId(Integer myalbumId, Long userId);

    // 특정 앨범의 멤버 수 세기
    long countByMyAlbum_MyalbumId(Integer myalbumId);

    //hw 그룹의 모든 멤버
    @Query("SELECT a.userEntity.userId FROM AlbumMembers a WHERE a.myAlbum.myalbumId = :groupId")
    List<Long> findUserIdsByMyAlbumId(@Param("groupId") Long groupId);
}
