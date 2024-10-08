package com.wegotoo.application.event;

import com.wegotoo.application.chat.request.ChatSendServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageSentEvent {

    private final Long sendUserId;

    private final Long chatRoomId;

    private final String message;

    @Builder
    private ChatMessageSentEvent(Long sendUserId, Long chatRoomId, String message) {
        this.sendUserId = sendUserId;
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

    public static ChatMessageSentEvent to(Long sendUserId, Long chatRoomId, String message) {
        return ChatMessageSentEvent.builder()
                .sendUserId(sendUserId)
                .chatRoomId(chatRoomId)
                .message(message)
                .build();
    }
}
