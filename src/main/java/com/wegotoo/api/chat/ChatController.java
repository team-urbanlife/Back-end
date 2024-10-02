package com.wegotoo.api.chat;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.chat.request.ChatSendRequest;
import com.wegotoo.application.CursorResponse;
import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.chat.response.ChatResponse;
import com.wegotoo.application.chat.ChatService;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.infra.resolver.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/v1/chat-rooms/{chatRoomId}/chats")
    public ApiResponse<CursorResponse<String, ChatResponse>> findChats(
            @Auth Long userId,
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestParam(value = "cursor", required = false) String cursorId,
            @RequestParam(value = "size", required = false, defaultValue = "50") Integer size
    ) {
        return ApiResponse.ok(chatService.findAllChats(userId, chatRoomId, cursorId, size));
    }

    @MessageMapping("/chat-rooms/{chatRoomId}/send")
    @SendTo("/topic/chat-rooms/{chatRoomId}")
    public ChatResponse sendChat(
            @Auth Long userId,
            @Payload ChatSendRequest request,
            @DestinationVariable("chatRoomId") Long chatRoomId
    ) {
        return chatService.sendChatMessage(userId, request.toService(chatRoomId));
    }

}
