package com.wegotoo.api.chat.request;

import com.wegotoo.application.chat.request.ChatSendServiceRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatSendRequest {

    private String message;

    @Builder
    private ChatSendRequest(String message) {
        this.message = message;
    }

    public ChatSendServiceRequest toService(String chatRoomCode) {
        return ChatSendServiceRequest.builder()
                .roomCode(chatRoomCode)
                .message(message)
                .build();
    }

}
