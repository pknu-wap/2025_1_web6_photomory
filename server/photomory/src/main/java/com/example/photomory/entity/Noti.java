package com.example.photomory.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTI")
public class Noti {

    @Id
    @Column(name = "noti_id")
    private Integer notiId;

    @Column(name = "noti_type", nullable = false)
    private String notiType;

    @Column(name = "noti_message", nullable = false)
    private String notiMessage;

    @Column(name = "noti_created_at", nullable = false)
    private LocalDateTime notiCreatedAt;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    // Getters and setters
    public Integer getNotiId() {
        return notiId;
    }

    public void setNotiId(Integer notiId) {
        this.notiId = notiId;
    }

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public String getNotiMessage() {
        return notiMessage;
    }

    public void setNotiMessage(String notiMessage) {
        this.notiMessage = notiMessage;
    }

    public LocalDateTime getNotiCreatedAt() {
        return notiCreatedAt;
    }

    public void setNotiCreatedAt(LocalDateTime notiCreatedAt) {
        this.notiCreatedAt = notiCreatedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
