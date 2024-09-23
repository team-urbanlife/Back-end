package com.wegotoo.api.chat;

import com.wegotoo.api.chat.request.ChatSendRequest;
import com.wegotoo.application.chat.ChatResponse;
import com.wegotoo.application.chat.ChatService;
import com.wegotoo.infra.resolver.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat-rooms/{roomCode}/send")
    @SendTo("/topic/chat-rooms/{roomCode}")
    public ChatResponse sendChat(
            @Auth Long userId,
            @Payload ChatSendRequest request,
            @DestinationVariable("roomCode") String roomCode
    ) {
        return chatService.sendChatMessage(userId, request.toService(roomCode));
    }

}
