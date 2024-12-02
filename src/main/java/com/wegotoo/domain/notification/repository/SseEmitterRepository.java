package com.wegotoo.domain.notification.repository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterRepository {

    private final Map<Long, SseEmitter> sseEmitterMap = new HashMap<>();

    public void save(Long userId, SseEmitter sseEmitter) {
        sseEmitterMap.put(userId, sseEmitter);
    }

    public void deleteByUserId(Long userId) {
        sseEmitterMap.remove(userId);
    }

    public SseEmitter findByUserId(Long userId) {
        return sseEmitterMap.get(userId);
    }

    public boolean containsEmitter(Long userId) {
        return sseEmitterMap.containsKey(userId);
    }

}
