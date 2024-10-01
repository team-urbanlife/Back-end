package com.wegotoo.application.chat.request;

import com.wegotoo.domain.chat.Chat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatSendServiceRequest {

    private Long chatRoomId;
    private String message;

    @Builder
    private ChatSendServiceRequest(Long chatRoomId, String message) {
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

    public Chat toDocument(Long senderId) {
        return Chat.builder()
                .senderId(senderId)
                .chatRoomId(chatRoomId)
                .message(message)
                .build();
    }

}
