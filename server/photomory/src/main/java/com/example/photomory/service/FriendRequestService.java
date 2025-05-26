package com.example.photomory.service;

import com.example.photomory.dto.FriendRequestDto;
import com.example.photomory.dto.FriendResponse;
import com.example.photomory.entity.Friend;
import com.example.photomory.entity.FriendRequest;
import com.example.photomory.entity.RequestStatus;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.FriendRequestRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final NotificationService notificationService;

    @Transactional
    public synchronized void sendRequest(Long senderId, Long receiverId) {
        System.out.println("💬 친구 요청 요청됨: " + senderId + " → " + receiverId);
        if (senderId.equals(receiverId)) throw new IllegalArgumentException("자기 자신에게 요청 불가");

        UserEntity sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("❌ sender 없음"));
        UserEntity receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("❌ receiver 없음"));

        if (friendRequestRepository.existsBySenderAndReceiverAndStatus(sender, receiver, RequestStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 친구 요청을 보냈습니다.");
        }

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        friendRequestRepository.save(request);

        notificationService.sendFriendRequestNotification(
                receiverId,
                sender.getUserName() + "님이 친구 요청을 보냈습니다.");
    }

    public void acceptRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("요청이 존재하지 않음"));

        request.setStatus(RequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        // sender, receiver 변수 선언
        UserEntity sender = request.getSender();
        UserEntity receiver = request.getReceiver();

        // 형변환 포함하여 사용, 친구 양방향 관계 설정
        friendRepository.save(new Friend((long) sender.getUserId(), (long) receiver.getUserId(), true));
        friendRepository.save(new Friend((long) receiver.getUserId(), (long) sender.getUserId(), true));

        notificationService.sendFriendRequestNotification(
                (long) sender.getUserId(),  // 요청 보낸 사람에게 알림 전송
                receiver.getUserName() + "님이 친구 요청을 수락했습니다."
        );
    }

    public void rejectRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow();
        request.setStatus(RequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }

    public List<FriendRequestDto> getReceivedRequests(Long userId) {
        UserEntity receiver = userRepository.findById(userId).orElseThrow();
        return friendRequestRepository.findByReceiverAndStatus(receiver, RequestStatus.PENDING)
                .stream()
                .map(r -> new FriendRequestDto(
                        r.getId(),
                        (long) r.getSender().getUserId(),   // 형변환 필요 (int → long)
                        (long) r.getReceiver().getUserId(), // 오타 수정 + 형변환
                        r.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    public List<FriendRequestDto> getSentRequests(Long userId) {
        UserEntity sender = userRepository.findById(userId).orElseThrow();
        return friendRequestRepository.findBySenderAndStatus(sender, RequestStatus.PENDING)
                .stream()
                .map(r -> new FriendRequestDto(
                        r.getId(),
                        (long) r.getSender().getUserId(),   // 형변환 필요 (int → long)
                        (long) r.getReceiver().getUserId(), // 오타 수정 + 형변환
                        r.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    public List<FriendResponse> getFriendsOf(Long userId) {
        // 1. 친구 목록 (내가 친구 맺은 사용자들의 ID)
        List<Friend> friends = friendRepository.findByFromUserIdAndAreWeFriendTrue(userId);
        List<Long> ids = friends.stream()
                .map(Friend::getToUserId)
                .collect(Collectors.toList());

        // 2. 친구들의 사용자 정보
        List<UserEntity> users = userRepository.findAllById(ids);

        // 3. FriendResponse 형태로 가공 (DTO 필드 순서에 맞춰서)
        return users.stream()
                .map(user -> new FriendResponse(
                        (long) user.getUserId(),           // userId
                        user.getUserName(),     // name
                        user.getUserEmail()  // 이메일
                ))
                .collect(Collectors.toList());
    }

    //양방향이라 두번삭제
    @Transactional
    public void deleteFriend(Long userId, Long targetId) {
        if (friendRepository.existsByFromUserIdAndToUserId(userId, targetId)) {
            friendRepository.deleteByFromUserIdAndToUserId(userId, targetId);
        }
        if (friendRepository.existsByFromUserIdAndToUserId(targetId, userId)) {
            friendRepository.deleteByFromUserIdAndToUserId(targetId, userId);
        }
    }
}

