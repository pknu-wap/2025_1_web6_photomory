package com.example.photomory.service;

import com.example.photomory.dto.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class SseEmitters {

    // 다중 연결을 지원하기 위해 List로 변경
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    // SSE 연결 추가 (다중 기기 지원)
    public void add(Long userId, SseEmitter emitter) {
        emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // 연결이 끊기거나 타임아웃되면 emitter 제거
        emitter.onCompletion(() -> remove(userId, emitter));
        emitter.onTimeout(() -> remove(userId, emitter));
        emitter.onError((e) -> {
            log.warn("SSE 연결 끊김/ 타임아웃 - userId: {}", userId, e);
            remove(userId, emitter);
        });
    }

    // 개별 emitter 제거
    public void remove(Long userId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

    // 알림 전송 (모든 연결에)
    public void send(Long userId, Object data) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(SseEmitter.event().data(data));
                } catch (IOException e) {
                    log.warn("SSE 전송(send) 실패 - userId: {}", userId, e);
                    remove(userId, emitter);
                }
            }
        }
    }

    // NotificationResponse를 이벤트 이름과 함께 전송
    public void send(Long userId, NotificationResponse response) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name(response.getType().name())
                            .data(response));
                } catch (IOException e) {
                    log.warn("SSE 전송(notiresponse) 실패 - userId: {}", userId, e);
                    remove(userId, emitter);
                }
            }
        }
    }

    // (선택) 현재 연결 상태 확인용
    public List<SseEmitter> get(Long userId) {
        return emitters.get(userId);
    }
}
