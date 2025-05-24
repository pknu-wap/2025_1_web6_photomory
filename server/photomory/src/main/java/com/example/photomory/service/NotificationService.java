package com.example.photomory.service;

import com.example.photomory.domain.Notification;
import com.example.photomory.domain.NotificationType;
import com.example.photomory.dto.NotificationRequest;
import com.example.photomory.dto.NotificationResponse;
import com.example.photomory.dto.RemindNotificationDto;
import com.example.photomory.entity.Photo;
import com.example.photomory.repository.ImageRepository;
import com.example.photomory.repository.NotificationRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    // 통합된 Emitter 저장소 (SseEmitters)
    private final SseEmitters sseEmitters;

    // 1. SSE 구독 - 모든 사용자 공통
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitters.add(userId, emitter); // 연결 등록

        // 연결 종료 또는 타임아웃 시 자동 제거
        emitter.onCompletion(() -> sseEmitters.remove(userId));
        emitter.onTimeout(() -> sseEmitters.remove(userId));
        emitter.onError(e -> sseEmitters.remove(userId));

        return emitter;
    }

    // 2. 일반 알림 전송 (댓글, 좋아요 등)
    public void send(NotificationRequest request) {
        // 1) 알림 DB 저장
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .message(request.getMessage())
                .type(request.getType())
                .createdAt(LocalDateTime.now())
                .isRead(false)  // 읽지 않은 상태로 저장
                .build();
        notificationRepository.save(notification);

        // 2) 해당 유저의 Emitter 찾기
        SseEmitter emitter = sseEmitters.get(request.getUserId());
        if (emitter != null) {
            try {
                int unreadCount = countUnread(request.getUserId()); // 읽지 않은 개수 계산

                // 3) 전송할 데이터 구성
                Map<String, Object> payload = Map.of(
                        "message", request.getMessage(),
                        "unreadCount", unreadCount
                );

                // 4) SSE 전송
                emitter.send(SseEmitter.event()
                        .name("notify")
                        .id(String.valueOf(notification.getId()))
                        .data(payload));

            } catch (IOException e) {
                sseEmitters.remove(request.getUserId());
            }
        }
    }

    // 3. 친구 요청 알림 전송 (간단한 메시지)
    public void sendFriendRequestNotification(Long receiverId, String message) {
        SseEmitter emitter = sseEmitters.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("friend-request").data(message));
            } catch (IOException e) {
                sseEmitters.remove(receiverId);
            }
        }
    }

    // 4. 추억 회상 알림 (3일 전 이미지 랜덤 전송)
    public void sendRemindNotification() {
        LocalDate targetDate = LocalDate.now().minusDays(3);

        // 3일 전 날짜의 모든 이미지 가져오기
        List<Photo> images = imageRepository.findByDate(targetDate);
        if (images.isEmpty()) return;

        // 무작위로 1장 선택
        Photo image = images.get(new Random().nextInt(images.size()));

        // DB 저장
        Notification notification = Notification.builder()
                .userId(image.getUserId())
                .type(NotificationType.REMIND)
                .message("3일 전 오늘")
                .photoUrl(image.getPhotoUrl())
                .title(image.getTitle())
                .date(image.getDate())
                .build();
        notificationRepository.save(notification);

        // 프론트로 보낼 DTO 생성
        RemindNotificationDto dto = RemindNotificationDto.builder()
                .type("REMIND")
                .title(image.getTitle())
                .photoUrl(image.getPhotoUrl())
                .date(image.getDate().toString())
                .message("3일 전 오늘")
                .build();

        // SSE 전송
        sseEmitters.send(image.getUserId(), dto);
    }

    // 안 읽은 알림 개수 반환
    public int countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // 알림 하나 읽음 처리
    @Transactional
    public void markAsRead(Long notiId) {
        notificationRepository.markAsRead(notiId);
    }

    // 모든 알림 읽음 처리
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    // 알림 전체 조회 + 모두 읽음 처리 + DTO 변환
    @Transactional
    public List<NotificationResponse> getNotificationsAndMarkAllRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        notificationRepository.markAllAsRead(userId); // 읽음 처리

        return notifications.stream().map(n -> NotificationResponse.builder()
                .id(n.getId())
                .message(n.getMessage())
                .type(n.getType())
                .createdAt(n.getCreatedAt())
                .isRead(true) // 이미 읽음 처리된 상태
                .build()).toList();
    }
}
