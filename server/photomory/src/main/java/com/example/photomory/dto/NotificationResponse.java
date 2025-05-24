package com.example.photomory.dto;

import com.example.photomory.domain.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;
    private boolean isRead;
}
