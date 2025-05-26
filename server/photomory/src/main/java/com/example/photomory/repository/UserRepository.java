package com.example.photomory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(Long userId);
    List<UserEntity> findByUserNameContaining(String keyword);
    Optional<UserEntity> findByUserEmail(String userEmail);
    
    Optional<UserEntity> findByUserEmail(String email);

    //hw
    // 친구가 아닌 사용자 검색 (키워드 포함)

    @Query("SELECT am.userEntity.userId FROM AlbumMembers am WHERE am.myAlbum.id = :groupId")
    List<Long> findUserIdsByGroupId(@Param("groupId") Long groupId);

    List<UserEntity> findAllByUserIdNotInAndUserIdNot(List<Long> excludedIds, Long loginUserId);
    List<UserEntity> findAllByUserIdNotInAndUserIdNotAndUserNameContainingIgnoreCase(List<Long> excludedIds, Long loginUserId, String keyword);
    List<UserEntity> findAllByUserIdNot(Long loginUserId);
    List<UserEntity> findAllByUserIdNotAndUserNameContainingIgnoreCase(Long loginUserId, String keyword);


}
