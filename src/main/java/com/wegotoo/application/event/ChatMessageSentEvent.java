package com.wegotoo.application.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageSentEvent {

    private final Long receiverId;

    private final String message;

    @Builder
    private ChatMessageSentEvent(Long receiverId, String message) {
        this.receiverId = receiverId;
        this.message = message;
    }

    public static ChatMessageSentEvent to(Long id, String message) {
        return ChatMessageSentEvent.builder()
                .receiverId(id)
                .message(message)
                .build();
    }
}
