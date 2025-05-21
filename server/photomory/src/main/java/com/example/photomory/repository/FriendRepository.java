package com.example.photomory.repository;

import com.example.photomory.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 친구 관계 리스트 (친구 상태가 true인 경우)
    List<Friend> findByFromUserIdAndAreWeFriendTrue(Long fromUserId);

    // 특정 두 사용자간 친구 관계 조회
    Optional<Friend> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    //hw-친추할때 추가
    void deleteByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    //jy
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    @Query("SELECT f.toUser FROM Friend f WHERE f.fromUser.userId = :userId")
    List<UserEntity> findFriendsByUserId(@Param("userId") Long userId);
}
