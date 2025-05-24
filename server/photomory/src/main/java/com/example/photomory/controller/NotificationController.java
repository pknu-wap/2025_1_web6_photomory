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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final SseEmitters sseEmitters;

    //알림목록조회 테스트용!!
    @GetMapping("/test")
    public ResponseEntity<List<NotificationResponse>> getTestNotifications(@RequestParam Long userId) {
        List<NotificationResponse> responses = notificationService.getNotificationsAndMarkAllRead(userId);
        return ResponseEntity.ok(responses);
    }

    //SSE 접속 테스트용!! 실사용전에 삭제
    @GetMapping(value = "/subscribe-test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter testSubscribe() {
        System.out.println("✅ /subscribe-test 호출됨");

        try {
            SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
            sseEmitters.add(1L, emitter);
            emitter.onCompletion(() -> System.out.println("🔚 SSE 연결 종료됨"));
            emitter.onTimeout(() -> System.out.println("⏱️ SSE 타임아웃"));
            emitter.onError(e -> System.out.println("❌ SSE 에러: " + e.getMessage()));
            return emitter;
        } catch (Exception e) {
            System.out.println("🔥 subscribe-test 에러: " + e.getMessage());
            throw e;
        }
    }


    // ✅ 토큰 기반 구독
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam("token") String token) {
        String email = jwtTokenProvider.extractUsername(token); // JWT로부터 이메일 추출

        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int userIdInt = user.getUserId();
        Long userId = Integer.toUnsignedLong(userIdInt); // int → Long 변환

        return notificationService.subscribe(userId);
    }

    // 알림 전송
    @PostMapping("/send")
    public void send(@RequestBody NotificationRequest request) {
        notificationService.send(request);
    }

    // 알림 전체 조회 + 읽음 처리
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
