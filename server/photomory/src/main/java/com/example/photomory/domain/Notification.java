package com.example.photomory.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @Column(name = "noti_created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); //DB 저장 전 자동으로 현재 시간 주입
    }

    @Column(name = "noti_is_read")
    private boolean isRead = false;

    @Column(name = "photo_url")
    private String photoUrl;

    private String title;
    private LocalDate date;

}
