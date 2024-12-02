package com.wegotoo.application.notification.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SandRequest {

    private final Long recipientId;
    private final Long chatRoomId;
    private final String message;

    @Builder
    private SandRequest(Long recipientId, Long chatRoomId, String message) {
        this.recipientId = recipientId;
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

    public static SandRequest of(Long recipientId, Long chatRoomId, String message) {
        return SandRequest.builder()
                .recipientId(recipientId)
                .chatRoomId(chatRoomId)
                .message(message)
                .build();
    }

}
