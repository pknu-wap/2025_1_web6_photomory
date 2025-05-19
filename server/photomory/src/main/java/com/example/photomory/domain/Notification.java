package com.example.photomory.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id")
    private Long id;

    @Column(name = "user_id")  // 알림 받는 사용자 ID (수신자)
    private Long userId;

    @Column(name = "noti_message")
    private String message;

    @Enumerated(EnumType.STRING)

    @Column(name = "noti_type")
    private NotificationType type;

    @Column(name = "noti_created_at")
    private LocalDateTime createdAt;

    @Column(name = "noti_is_read")
    private boolean isRead = false;
}
