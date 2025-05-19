package com.example.photomory.repository;

import com.example.photomory.entity.FriendRequest;
import com.example.photomory.entity.RequestStatus;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderAndReceiverAndStatus(UserEntity sender, UserEntity receiver, RequestStatus status);
    List<FriendRequest> findByReceiverAndStatus(UserEntity receiver, RequestStatus status);
    List<FriendRequest> findBySenderAndStatus(UserEntity sender, RequestStatus status);
}
