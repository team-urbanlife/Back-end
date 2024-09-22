package com.wegotoo.application.chat.request;

import com.wegotoo.domain.chat.Chat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatSendServiceRequest {

    private String message;
    private String roomCode;

    @Builder
    private ChatSendServiceRequest(String message, String roomCode) {
        this.message = message;
        this.roomCode = roomCode;
    }

    public Chat toDocument(Long senderId) {
        return Chat.builder()
                .senderId(senderId)
                .message(message)
                .roomCode(roomCode)
                .build();
    }

}
