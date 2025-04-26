package com.example.photomory.repository;

import com.example.photomory.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByFromUserIdAndAreWeFriendTrue(Long fromUserId);
    Optional<Friend> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}

