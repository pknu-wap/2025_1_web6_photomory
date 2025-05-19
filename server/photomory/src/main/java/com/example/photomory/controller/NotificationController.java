package com.example.photomory.controller;

import com.example.photomory.dto.NotificationRequest;
import com.example.photomory.dto.NotificationResponse;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.UserRepository;
import com.example.photomory.security.JwtTokenProvider;
import com.example.photomory.service.NotificationService;
import com.example.photomory.service.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    //얘네는 친구추가하면서
    private final SseEmitters emitters;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(userId, emitter);
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onCompletion(() -> emitters.remove(userId));
        return emitter;
    }

    //여기부터 1차
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // ✅ 쿼리 파라미터로 토큰을 받아 이메일 → userId 추출
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam("token") String token) {
        String email = jwtTokenProvider.extractUsername(token); // JWT로부터 이메일 추출

        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int userIdInt = user.getUserId();
        Long userId = Integer.toUnsignedLong(userIdInt); // int → Long 변환

        return notificationService.subscribe(userId);
    }

    @PostMapping("/send")
    public void send(@RequestBody NotificationRequest request) {
        notificationService.send(request);
    }

    @GetMapping
    public List<NotificationResponse> getNotifications(@RequestParam("token") String token) {
        String email = jwtTokenProvider.extractUsername(token);
        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int userIdInt = user.getUserId();
        Long userId = Integer.toUnsignedLong(userIdInt);

        return notificationService.getNotificationsAndMarkAllRead(userId);
    }
}
