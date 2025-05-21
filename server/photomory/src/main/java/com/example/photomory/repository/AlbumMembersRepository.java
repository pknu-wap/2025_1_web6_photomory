package com.example.photomory.repository;

import com.example.photomory.entity.AlbumMembers;
import com.example.photomory.entity.AlbumMembers.AlbumMembersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumMembersRepository extends JpaRepository<AlbumMembers, AlbumMembersId> {

    /**
     * 특정 유저가 특정 그룹(마이앨범)에 속해 있는지 확인
     *
     * @param userId 유저 ID
     * @param myalbumId 그룹 ID
     * @return 존재 여부
     */
    boolean existsByUserEntityUserIdAndMyAlbumMyalbumId(Integer userId, Integer myalbumId);

    /**
     * 특정 그룹에 속한 모든 멤버 조회
     *
     * @param myalbumId 그룹 ID
     * @return 해당 그룹의 모든 멤버 목록
     */
    List<AlbumMembers> findByMyAlbum_MyalbumId(Integer myalbumId);

    /**
     * 특정 유저가 속한 모든 그룹(마이앨범) 목록 조회
     *
     * @param userId 유저 ID
     * @return 유저가 속한 그룹들의 AlbumMembers 목록
     */
    List<AlbumMembers> findByUserEntityUserId(Long userId);
}
