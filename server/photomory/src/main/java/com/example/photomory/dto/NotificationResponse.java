package com.example.photomory.dto;

import com.example.photomory.domain.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long id;              // 알림 ID
    private Long userId;          // 수신자 ID (나)
    private Long senderId;        // 발신자 ID
    private String senderName;    // 발신자 이름
    private String senderPhotourl; // 발신자 프로필 사진
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;
    private boolean isRead;
    private Long requestId;
}

