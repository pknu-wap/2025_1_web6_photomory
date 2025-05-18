package com.example.photomory.repository;

import com.example.photomory.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 친구 관계 리스트 (친구 상태가 true인 경우)
    List<Friend> findByFromUserIdAndAreWeFriendTrue(Long fromUserId);

    // 특정 두 사용자간 친구 관계 조회
    Optional<Friend> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    // 특정 두 사용자간 친구 관계 존재 여부 체크
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

}
