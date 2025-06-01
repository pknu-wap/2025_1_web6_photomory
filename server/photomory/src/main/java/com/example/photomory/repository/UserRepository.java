package com.example.photomory.repository;

import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long userId);

    List<UserEntity> findByUserNameContaining(String keyword);

    Optional<UserEntity> findByUserEmail(String userEmail);

    // 그룹(우리 앨범) ID를 기준으로 속한 사용자 ID 조회
    @Query("SELECT am.userEntity.userId FROM AlbumMembers am WHERE am.ourAlbum.albumId = :groupId")
    List<Long> findUserIdsByGroupId(@Param("groupId") Long groupId);

    // 친구 아닌 사용자 조회
    List<UserEntity> findAllByUserIdNotInAndUserIdNot(List<Long> excludedIds, Long loginUserId);

    List<UserEntity> findAllByUserIdNotInAndUserIdNotAndUserNameContainingIgnoreCase(
            List<Long> excludedIds, Long loginUserId, String keyword
    );

    List<UserEntity> findAllByUserIdNot(Long loginUserId);

    List<UserEntity> findAllByUserIdNotAndUserNameContainingIgnoreCase(Long loginUserId, String keyword);
}
