package com.example.photomory.service;

import com.example.photomory.domain.Notification;
import com.example.photomory.domain.NotificationType;
import com.example.photomory.dto.NotificationResponse;
import com.example.photomory.dto.RemindNotificationDto;
import com.example.photomory.entity.Friend;
import com.example.photomory.entity.Photo;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.ImageRepository;
import com.example.photomory.repository.NotificationRepository;
import com.example.photomory.repository.UserRepository;
import com.example.photomory.service.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final SseEmitters emitters;
    private final FriendRepository friendRepository;

    // 1. SSE 구독
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        return emitter;
    }

    // 2. Remind를 제외한 모든 알림 전송 (댓글/좋아요/친구요청/수락)
    public void sendNotification(Long receiverId, String message, NotificationType type, Long requestId) {
        // 1) DB 저장
        Notification notification = Notification.builder()
                .userId(receiverId)
                .message(message)
                .type(type)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .requestId(requestId) // null이면 일반 알림, 아니면 친구요청 관련
                .build();
        notificationRepository.save(notification);

        // 2) 프론트 전송 DTO
        NotificationResponse response = NotificationResponse.builder()
                .id(notification.getId())
                .userId(receiverId)
                .message(message)
                .type(type)
                .createdAt(notification.getCreatedAt())
                .isRead(false)
                .requestId(requestId)
                .build();

        // 3) SSE 전송
        emitters.send(receiverId, response);
    }

    // ✅ 3. Remind 알림만 따로 관리
    public void sendRemindNotification() {
        LocalDate targetDate = LocalDate.now().minusDays(3);
        List<Photo> images = imageRepository.findByDate(targetDate);
        if (images.isEmpty()) return;

        Photo image = images.get(new Random().nextInt(images.size()));

        // DB 저장
        Notification notification = Notification.builder()
                .userId(image.getUserId())
                .type(NotificationType.REMIND)
                .message("3일 전 오늘")
                .photoUrl(image.getPhotoUrl())
                .title(image.getTitle())
                .date(image.getDate())
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        notificationRepository.save(notification);

        // 프론트 DTO
        RemindNotificationDto dto = RemindNotificationDto.builder()
                .type("REMIND")
                .title(image.getTitle())
                .photoUrl(image.getPhotoUrl())
                .date(image.getDate().toString())
                .message("3일 전 오늘")
                .build();

        emitters.send(image.getUserId(), dto);
    }

    // ✅ 4. 안 읽은 알림 개수 조회
    public int countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // ✅ 5. 알림 읽음 처리
    @Transactional
    public void markAsRead(Long notiId) {
        notificationRepository.markAsRead(notiId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    // ✅ 6. 알림 전체 조회 + 읽음 처리 + DTO 변환
    @Transactional
    public List<NotificationResponse> getNotificationsAndMarkAllRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        notificationRepository.markAllAsRead(userId);

        return notifications.stream()
                .map(n -> NotificationResponse.builder()
                        .id(n.getId())
                        .userId(n.getUserId())
                        .message(n.getMessage())
                        .type(n.getType())
                        .createdAt(n.getCreatedAt())
                        .isRead(true)
                        .requestId(n.getRequestId())
                        .build())
                .toList();
    }
}
