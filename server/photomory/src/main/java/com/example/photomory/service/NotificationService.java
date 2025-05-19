package com.example.photomory.service;


import com.example.photomory.domain.Notification;
import com.example.photomory.dto.NotificationRequest;
import com.example.photomory.dto.NotificationResponse;
import com.example.photomory.repository.NotificationRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;

    private final SseEmitters emitters; //이건 친구추가알림하면서
    public void sendFriendRequestNotification(Long receiverId, String message) {
        SseEmitter emitter = emitters.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("friend-request").data(message));
            } catch (IOException e) {
                emitters.remove(receiverId);
            }
        }
    }

    //여기부터가 1차
    // ✅ SSE 연결 구독
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.remove(userId));
        emitter.onTimeout(() -> emitterRepository.remove(userId));

        return emitter;
    }

    // ✅ 알림 전송 및 DB 저장 + SSE 알림 발송 + 읽지 않은 개수 포함
    public void send(NotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .message(request.getMessage())
                .type(request.getType())
                .createdAt(LocalDateTime.now())
                .isRead(false)  // ✅ 읽지 않은 상태로 저장
                .build();

        notificationRepository.save(notification);

        SseEmitter emitter = emitterRepository.get(request.getUserId());
        if (emitter != null) {
            try {
                int unreadCount = countUnread(request.getUserId()); // ✅ 읽지 않은 개수 계산

                Map<String, Object> payload = Map.of(
                        "message", request.getMessage(),
                        "unreadCount", unreadCount
                );

                emitter.send(SseEmitter.event()
                        .name("notify")
                        .id(String.valueOf(notification.getId()))
                        .data(payload)); // ✅ message + unreadCount 포함

            } catch (IOException e) {
                emitterRepository.remove(request.getUserId());
            }
        }
    }

    // ✅ 읽지 않은 알림 개수 반환
    public int countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // ✅ 알림 하나 읽음 처리
    @Transactional
    public void markAsRead(Long notiId) {
        notificationRepository.markAsRead(notiId);
    }

    // ✅ 해당 유저의 모든 알림 읽음 처리
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    public List<NotificationResponse> getNotificationsAndMarkAllRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // 읽음 처리
        notificationRepository.markAllAsRead(userId);

        // DTO 변환
        return notifications.stream().map(n -> NotificationResponse.builder()
                .id(n.getId())
                .message(n.getMessage())
                .type(n.getType())
                .createdAt(n.getCreatedAt())
                .isRead(true) // 이미 읽음 처리된 상태
                .build()
        ).toList();
    }


}
