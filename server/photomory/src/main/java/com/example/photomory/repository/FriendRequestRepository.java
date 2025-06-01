package com.example.photomory.repository;

import com.example.photomory.entity.Friend;
import com.example.photomory.entity.FriendRequest;
import com.example.photomory.entity.RequestStatus;
import com.example.photomory.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderAndReceiverAndStatus(UserEntity sender, UserEntity receiver, RequestStatus status);
    List<FriendRequest> findByReceiverAndStatus(UserEntity receiver, RequestStatus status);
    List<FriendRequest> findBySenderAndStatus(UserEntity sender, RequestStatus status);
    // 친구 요청을 보낸 사용자 ID로 요청 목록 찾기
    List<FriendRequest> findBySender(UserEntity sender);

}
