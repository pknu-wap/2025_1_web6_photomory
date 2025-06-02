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

    // 사용자별로 여러 개의 SSE 연결을 관리
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    /**
     * 사용자별로 SSE Emitter 추가
     */
    public void add(Long userId, SseEmitter emitter) {
        emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // 연결 종료/타임아웃/오류 시 자동으로 제거
        emitter.onCompletion(() -> remove(userId, emitter));
        emitter.onTimeout(() -> remove(userId, emitter));
        emitter.onError(e -> {
            log.warn("SSE 연결 오류 - userId: {}", userId, e);
            remove(userId, emitter);
        });
    }

    /**
     * 특정 emitter 제거
     */
    public void remove(Long userId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

    /**
     * 사용자에게 단순 데이터 전송
     */
    public void send(Long userId, Object data) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(SseEmitter.event().data(data));
                } catch (IllegalStateException e) {
                    log.warn("SSE 이미 완료됨 - userId: {}", userId, e);
                    remove(userId, emitter);
                } catch (IOException e) {
                    log.warn("SSE 전송 실패 - userId: {}", userId, e);
                    remove(userId, emitter);
                }
            }
        }
    }

    /**
     * NotificationResponse를 이벤트 이름과 함께 전송
     */
    public void send(Long userId, NotificationResponse response) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name(response.getType().name())
                            .data(response));
                } catch (IllegalStateException e) {
                    log.warn("SSE 이미 완료됨 - userId: {}", userId, e);
                    remove(userId, emitter);
                } catch (IOException e) {
                    log.warn("SSE 전송 실패 - userId: {}", userId, e);
                    remove(userId, emitter);
                }
            }
        }
    }

    /**
     * 현재 연결된 emitter 리스트 반환 (선택적 디버그용)
     */
    public List<SseEmitter> get(Long userId) {
        return emitters.get(userId);
    }
}