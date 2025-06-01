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

    @Column(name = "sender_id")  // 보낸 사용자 ID (발신자)
    private Long senderId;


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

    private String senderName;    // 발신자 이름
    private String senderPhotourl; // 발신자 프로필 사진

    private String title;
    private LocalDate date;

    private Long requestId;


}
