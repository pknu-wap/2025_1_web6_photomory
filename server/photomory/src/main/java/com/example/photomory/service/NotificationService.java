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

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final SseEmitters emitters;

    // 1. SSE 구독
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(userId, emitter);
        return emitter;
    }

    // 2. Remind를 제외한 모든 알림 전송 (발신자 정보 포함)
    public void sendNotification(Long receiverId, Long senderId, String message, NotificationType type, Long requestId) {
        // 1) DB 저장
        Notification notification = Notification.builder()
                .userId(receiverId)
                .senderId(senderId)
                .message(message)
                .type(type)
                .requestId(requestId)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        notificationRepository.save(notification);

        // 2) 발신자 정보 조회
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("발신자 정보 없음"));

        // 3) DTO 생성
        NotificationResponse response = NotificationResponse.builder()
                .id(notification.getId())
                .userId(receiverId) //알림받는사람
                .senderId(senderId) //알림보내는사람
                .senderName(sender.getUserName())
                .senderPhotourl(sender.getUserPhotourl())
                .message(message)
                .type(type)
                .requestId(requestId)
                .createdAt(notification.getCreatedAt())
                .isRead(false)
                .build();

        // 4) SSE 전송
        emitters.send(receiverId, response);
    }

    // 3. Remind 알림 (발신자 정보 불필요)
    public void sendRemindNotification() {
        LocalDate targetDate = LocalDate.now().minusDays(3);
        List<Photo> images = imageRepository.findByDate(targetDate);
        if (images.isEmpty()) return;

        Photo image = images.get(new Random().nextInt(images.size()));

        Notification notification = Notification.builder()
                .userId(image.getUserId())
                .type(NotificationType.REMIND)
                .message("3일 전 오늘")
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        notificationRepository.save(notification);

        // RemindNotificationDto로 전송
        RemindNotificationDto dto = RemindNotificationDto.builder()
                .type("REMIND")
                .title(image.getTitle())
                .photoUrl(image.getPhotoUrl())
                .date(image.getDate().toString())
                .message("3일 전 오늘")
                .build();

        emitters.send(image.getUserId(), dto);
    }

    // 4. 안 읽은 알림 개수
    public int countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // 5. 전체 알림 조회 + 읽음 처리 + 발신자 정보 포함
    @Transactional
    public List<NotificationResponse> getNotificationsAndMarkAllRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        notificationRepository.markAllAsRead(userId);

        return notifications.stream()
                .map(n -> {
                    // Remind 알림은 senderId가 없을 수 있으므로 처리 분기
                    if (n.getSenderId() == null) {
                        // 발신자 정보 없는 경우 (Remind) → senderName, senderPhotourl는 null로 전달
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

                    // 발신자 정보 있는 경우
                    UserEntity sender = userRepository.findById(n.getSenderId())
                            .orElseThrow(() -> new RuntimeException("발신자 정보 없음"));

                    return NotificationResponse.builder()
                            .id(n.getId())
                            .userId(n.getUserId())
                            .senderId(n.getSenderId())
                            .senderName(sender.getUserName())
                            .senderPhotourl(sender.getUserPhotourl())
                            .message(n.getMessage())
                            .type(n.getType())
                            .createdAt(n.getCreatedAt())
                            .isRead(true)
                            .requestId(n.getRequestId())
                            .build();
                })
                .toList();
    }
}
