package com.wegotoo.application.event;

import com.wegotoo.application.chat.request.ChatSendServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageSentEvent {

    private final Long receiverId;

    private final ChatSendServiceRequest request;

    @Builder
    private ChatMessageSentEvent(Long receiverId, ChatSendServiceRequest request) {
        this.receiverId = receiverId;
        this.request = request;
    }

    public static ChatMessageSentEvent to(Long id, ChatSendServiceRequest request) {
        return ChatMessageSentEvent.builder()
                .receiverId(id)
                .request(request)
                .build();
    }
}
