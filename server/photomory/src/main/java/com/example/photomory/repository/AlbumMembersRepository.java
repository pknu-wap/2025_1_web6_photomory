package com.example.photomory.repository;

import com.example.photomory.entity.AlbumMembers;
import com.example.photomory.entity.AlbumMembers.AlbumMembersId;
import com.example.photomory.entity.UserGroup;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumMembersRepository extends JpaRepository<AlbumMembers, AlbumMembersId> {

    // 특정 유저가 특정 그룹에 속해있는지 확인
    boolean existsByUserEntityAndUserGroup(UserEntity userEntity, UserGroup userGroup);

    // 특정 그룹에 속한 모든 AlbumMembers 조회
    List<AlbumMembers> findByUserGroup_Id(Long groupId);

    // 특정 유저가 속한 모든 그룹 멤버십 조회
    List<AlbumMembers> findByUserEntity_UserId(Long userId);

    // 특정 그룹, 특정 유저에 해당하는 AlbumMembers 조회
    Optional<AlbumMembers> findByUserGroup_IdAndUserEntity_UserId(Long groupId, Long userId);

    // 특정 그룹에 속한 멤버 수 조회
    long countByUserGroup_Id(Long groupId);

    // 특정 그룹에 속한 유저 ID 리스트 조회
    @Query("SELECT am.userEntity.userId FROM AlbumMembers am WHERE am.userGroup.id = :groupId")
    List<Long> findUserIdsByUserGroupId(@Param("groupId") Long groupId);

    // 특정 유저가 속한 그룹 멤버십 중 그룹인 것들만 조회
    @Query("SELECT am FROM AlbumMembers am WHERE am.userEntity.userId = :userId AND am.userGroup.isGroup = true")
    List<AlbumMembers> findGroupMembershipsByUserId(@Param("userId") Long userId);

}
