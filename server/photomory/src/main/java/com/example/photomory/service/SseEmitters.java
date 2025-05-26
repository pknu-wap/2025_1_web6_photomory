package com.example.photomory.service;

import com.example.photomory.domain.NotificationType;
import com.example.photomory.dto.NotificationResponse;
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
    }

    // SSE 연결 제거
    public void remove(Long userId) {
        emitters.remove(userId);
    }


    public void send(Long userId, Object data) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(data));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }

    public void send(Long userId, NotificationResponse response) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name(response.getType().name())  // 알림 타입으로 이벤트 이름 지정
                        .data(response));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }

    public SseEmitter get(Long userId) {
        return emitters.get(userId);
    }
}