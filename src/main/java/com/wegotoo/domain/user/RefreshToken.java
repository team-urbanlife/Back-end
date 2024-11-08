package com.wegotoo.domain.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refresh", timeToLive = 604800)
public class RefreshToken {

    @Id
    private Long id;

    private String token;

    @Builder
    private RefreshToken(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public static RefreshToken create(Long userId, String token) {
        return RefreshToken.builder()
                .id(userId)
                .token(token)
                .build();
    }

    public void updateToken(String token) {
        this.token = token;
    }

}
