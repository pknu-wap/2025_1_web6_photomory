package com.example.photomory.service;

import com.example.photomory.dto.NonFriendUserDto;
import com.example.photomory.entity.RequestStatus;
import com.example.photomory.domain.NotificationType;
import com.example.photomory.entity.Friend;
import com.example.photomory.entity.FriendRequest;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.FriendRequestRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final NotificationService notificationService;

    // 친구 요청 보내기
    @Transactional
    public Long sendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) throw new IllegalArgumentException("자기 자신에게 요청 불가!");

        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("sender 없음"));
        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("receiver 없음"));

        // 이미 요청했는지 확인
        if (friendRequestRepository.existsBySenderAndReceiverAndStatus(sender, receiver, RequestStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 친구 요청을 보냈습니다.");
        }

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        friendRequestRepository.save(request);

        // 친구 요청 알림 전송
        String message = sender.getUserName() + "님이 친구 요청을 보냈습니다.";
        notificationService.sendNotification(receiverId, senderId, message, NotificationType.FRIEND_REQUEST, request.getId());

        // 요청 ID 반환
        return request.getId();
    }

    // 친구 요청 수락
    @Transactional
    public void acceptRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("요청이 존재하지 않음"));

        request.setStatus(RequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        UserEntity sender = request.getSender();
        UserEntity receiver = request.getReceiver();

        // 친구 양방향 저장
        friendRepository.save(new Friend(sender.getUserId(), receiver.getUserId(), true));
        friendRepository.save(new Friend(receiver.getUserId(), sender.getUserId(), true));

        // 🔥 친구 수락 알림 전송
        String message = receiver.getUserName() + "님이 친구 요청을 수락했습니다.";
        notificationService.sendNotification(sender.getUserId(),  receiver.getUserId(), message, NotificationType.FRIEND_ACCEPT, requestId);
    }

    // 친구 요청 거절
    @Transactional
    public void rejectRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("요청이 존재하지 않음"));

        request.setStatus(RequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }

    // 친구가 아닌 사용자 목록 조회
    public List<NonFriendUserDto> searchNonFriendUsers(Long loginUserId, String keyword) {
        // 친구 관계 조회
        List<Friend> friends = friendRepository.findByUserIdAndAreWeFriendTrue(loginUserId);
        List<Long> friendIds = friends.stream()
                .map(friend -> friend.getFromUserId().equals(loginUserId) ? friend.getToUserId() : friend.getFromUserId())
                .toList();

        //친구 요청 중인 사용자 조회
        UserEntity loginUser = userRepository.findById(loginUserId)
                .orElseThrow(() -> new RuntimeException("로그인 사용자 없음"));

        List<FriendRequest> requests = friendRequestRepository.findBySender(loginUser);
        List<Long> requestedIds = requests.stream()
                .map(FriendRequest::getReceiverId)
                .toList();

        // 제외할 사용자 ID 목록 생성 (친구 + 친구 요청 대상)
        Set<Long> excludedIds = new HashSet<>(friendIds);
        excludedIds.addAll(requestedIds);

        // 만약 친구나 요청 대상이 없으면 excludedIds를 비워서 모든 사용자 중 나만 제외
        List<UserEntity> users;
        if (excludedIds.isEmpty()) {
            if (keyword == null || keyword.isEmpty()) {
                users = userRepository.findAllByUserIdNot(loginUserId);
            } else {
                users = userRepository.findAllByUserIdNotAndUserNameContainingIgnoreCase(loginUserId, keyword);
            }
        } else {
            if (keyword == null || keyword.isEmpty()) {
                users = userRepository.findAllByUserIdNotInAndUserIdNot(new ArrayList<>(excludedIds), loginUserId);
            } else {
                users = userRepository.findAllByUserIdNotInAndUserIdNotAndUserNameContainingIgnoreCase(
                        new ArrayList<>(excludedIds), loginUserId, keyword);
            }
        }

        // DTO로 변환
        return users.stream()
                .map(user -> new NonFriendUserDto(
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserJob(),
                        user.getUserPhotourl()
                ))
                .toList();
    }


}
