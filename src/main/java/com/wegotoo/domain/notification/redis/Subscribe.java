package com.wegotoo.domain.notification.redis;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "subscribe", timeToLive = 604800)
public class Subscribe {

    @Id
    private Long userId;

    @Builder
    private Subscribe(Long userId, Long lastEventTime) {
        this.userId = userId;
    }

    public static Subscribe create(Long userId) {
        return Subscribe.builder()
                .userId(userId)
                .build();
    }

}
