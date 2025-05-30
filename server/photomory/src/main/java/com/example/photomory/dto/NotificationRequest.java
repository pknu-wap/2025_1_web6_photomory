package com.example.photomory.dto;

import com.example.photomory.domain.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NotificationRequest {
    private Long userId;// 알림 수신자
    private Long senderId;
    private String message;
    private NotificationType type;
}
