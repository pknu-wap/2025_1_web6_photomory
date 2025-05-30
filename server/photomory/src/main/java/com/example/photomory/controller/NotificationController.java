package com.example.photomory.controller;

import com.example.photomory.dto.NotificationResponse;
import com.example.photomory.service.AuthService;
import com.example.photomory.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthService authService;

    //1. SSE 구독 (토큰 기반)
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader("Authorization") String token) {
        Long userId = authService.extractUserId(token);
        return notificationService.subscribe(userId);
    }

    // 안 읽은 알림 개수 조회
    @GetMapping("/unread-count")
    public int countUnread(@RequestHeader("Authorization") String token) {
        Long userId = authService.extractUserId(token);
        return notificationService.countUnread(userId);
    }

    // 알림 목록 조회 + 읽음 처리
    @GetMapping("/list-read")
    public List<NotificationResponse> getNotifications(@RequestHeader("Authorization") String token) {
        Long userId = authService.extractUserId(token);
        return notificationService.getNotificationsAndMarkAllRead(userId);
    }
}
