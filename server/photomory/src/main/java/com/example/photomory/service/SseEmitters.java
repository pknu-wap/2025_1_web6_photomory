package com.example.photomory.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class SseEmitters {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // SSE 연결 추가, 종료 이벤트 등록
    public void add(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));
    }

    public SseEmitter get(Long userId) {
        return emitters.get(userId);
    }

    // SSE 연결 제거
    public void remove(Long userId) {
        emitters.remove(userId);
    }

    // 특정 사용자에게 알림 전송
    public void send(Long userId, Object data) {
    SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
        try {
            emitter.send(SseEmitter.event().name("remind").data(data));
        } catch (IOException e) {
            emitters.remove(userId);
        }
    }
}
}
