package com.wegotoo.domain.notification.redis;

import com.wegotoo.application.notification.request.SandRequest;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "notification")
public class Notification {

    @Id
    private Long userId;

    private Long chatRoomId;

    private String message;

    private LocalDateTime createdTime;

    private Type type;

    @Builder
    private Notification(Long userId, Long chatRoomId, String message, LocalDateTime createdTime, Type type) {
        this.userId = userId;
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.createdTime = createdTime;
        this.type = type;
    }

    public static Notification create(SandRequest request, LocalDateTime now, Type type) {
        return Notification.builder()
                .userId(request.getChatRoomId())
                .chatRoomId(request.getChatRoomId())
                .message(request.getMessage())
                .createdTime(now)
                .type(type)
                .build();
    }

}