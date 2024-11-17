package com.wegotoo.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "online:user")
public class OnlineUser {

    @Id
    private Long userId;
    private Long chatRoomId;

    @Builder
    private OnlineUser(Long userId, Long chatRoomId) {
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }

    public static OnlineUser create(Long userId, Long chatRoomId) {
        return OnlineUser.builder()
                .userId(userId)
                .chatRoomId(chatRoomId)
                .build();
    }

}
