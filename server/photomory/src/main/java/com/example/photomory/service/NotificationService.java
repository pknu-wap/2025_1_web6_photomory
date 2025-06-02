package com.example.photomory.service;

import com.example.photomory.domain.Notification;
import com.example.photomory.domain.NotificationType;
import com.example.photomory.dto.NotificationResponse;
import com.example.photomory.dto.RemindNotificationDto;
import com.example.photomory.entity.Photo;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.ImageRepository;
import com.example.photomory.repository.NotificationRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final SseEmitters emitters;
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    //SSE 구독
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(0L); // 무제한 연결
        emitters.add(userId, emitter);
        return emitter;
    }

    // 일반 알림 전송 (발신자 정보 포함)
    public void sendNotification(Long receiverId, Long senderId, String message, NotificationType type, Long requestId) {

        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("발신자 정보 없음"));

        // 알림 저장
        Notification notification = Notification.builder()
                .userId(receiverId)
                .senderId(senderId)
                .senderName(sender.getUserName())
                .senderPhotourl(sender.getUserPhotourl())
                .message(message)
                .type(type)
                .requestId(requestId)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        notificationRepository.save(notification);

        // DTO로 변환
        NotificationResponse response = NotificationResponse.builder()
                .id(notification.getId())
                .userId(receiverId)
                .senderId(senderId)
                .senderName(sender.getUserName())
                .senderPhotourl(sender.getUserPhotourl())
                .message(message)
                .type(type)
                .requestId(requestId)
                .createdAt(notification.getCreatedAt())
                .isRead(false)
                .build();

        // SSE 전thd
        log.info("Sending friend request notification to userId: {}", receiverId);
        emitters.send(receiverId, response);
    }

    // Remind 알림 전송 (발신자 정보 X)
    public void sendRemindNotification() {
        LocalDate targetDate = LocalDate.now().minusDays(3);
        List<Photo> images = imageRepository.findByDate(targetDate);
        if (images.isEmpty()) return;

        Photo image = images.get(new Random().nextInt(images.size()));

        // 알림 저장
        Notification notification = Notification.builder()
                .userId(image.getUserId())
                .type(NotificationType.REMIND)
                .message("3일 전 오늘")
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        notificationRepository.save(notification);

        // RemindNotificationDto 생성
        RemindNotificationDto dto = RemindNotificationDto.builder()
                .type("REMIND")
                .title(image.getTitle())
                .photoUrl(image.getPhotoUrl())
                .date(image.getDate().toString())
                .message("3일 전 오늘")
                .build();

        // SSE 전송
        emitters.send(image.getUserId(), dto);
    }

    // 안 읽은 알림 개수
    public int countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // 전체 알림 조회 + 읽음 처리
    @Transactional
    public List<NotificationResponse> getNotificationsAndMarkAllRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        notificationRepository.markAllAsRead(userId);

        return notifications.stream()
                .map(n -> {
                    // Remind 알림 처리
                    if (n.getSenderId() == null) {
                        return NotificationResponse.builder()
                                .id(n.getId())
                                .userId(n.getUserId())
                                .senderId(null)
                                .senderName(null)
                                .senderPhotourl(null)
                                .message(n.getMessage())
                                .type(n.getType())
                                .createdAt(n.getCreatedAt())
                                .isRead(true)
                                .requestId(n.getRequestId())
                                .build();
                    }

                    // 일반 알림은 발신자 정보 채워줌
                    UserEntity sender = userRepository.findById(n.getSenderId())
                            .orElse(null);

                    return NotificationResponse.builder()
                            .id(n.getId())
                            .userId(n.getUserId())
                            .senderId(n.getSenderId())
                            .senderName(sender != null ? sender.getUserName() : null)
                            .senderPhotourl(sender != null ? sender.getUserPhotourl() : null)
                            .message(n.getMessage())
                            .type(n.getType())
                            .createdAt(n.getCreatedAt())
                            .isRead(true)
                            .requestId(n.getRequestId())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
