package com.wegotoo.application.notification.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendRequest {

    private final Long recipientId;
    private final Long chatRoomId;
    private final String message;

    @Builder
    private SendRequest(Long recipientId, Long chatRoomId, String message) {
        this.recipientId = recipientId;
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

    public static SendRequest of(Long recipientId, Long chatRoomId, String message) {
        return SendRequest.builder()
                .recipientId(recipientId)
                .chatRoomId(chatRoomId)
                .message(message)
                .build();
    }

}
