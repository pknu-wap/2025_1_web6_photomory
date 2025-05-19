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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final NotificationService notificationService;

    public void sendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) throw new IllegalArgumentException("자기 자신에게 요청 불가");

        UserEntity sender = userRepository.findById(senderId).orElseThrow();
        UserEntity receiver = userRepository.findById(receiverId).orElseThrow();

        if (friendRequestRepository.existsBySenderAndReceiverAndStatus(sender, receiver, RequestStatus.PENDING)) {
            throw new IllegalStateException("이미 요청 보냄");
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

        // ✅ sender, receiver 변수 선언
        UserEntity sender = request.getSender();
        UserEntity receiver = request.getReceiver();

        // ✅ 형변환 포함하여 사용
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
                        user.getUserPhotourl()  // photourl
                ))
                .collect(Collectors.toList());
    }

    public void deleteFriend(Long userId, Long targetId) {
        friendRepository.deleteByFromUserIdAndToUserId(userId, targetId);
        friendRepository.deleteByFromUserIdAndToUserId(targetId, userId);
    }
}

