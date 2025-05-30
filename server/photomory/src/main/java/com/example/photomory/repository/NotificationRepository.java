package com.example.photomory.repository;

import com.example.photomory.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId); //remind할떄도 필요
    List<Notification> findByUserIdAndIdGreaterThan(Long userId, Long lastEventId);

    // 읽지 않은 알림 개수
    int countByUserIdAndIsReadFalse(Long userId);

    // 전체 알림 읽음 처리
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.userId = :userId")
    void markAllAsRead(@Param("userId") Long userId);
}