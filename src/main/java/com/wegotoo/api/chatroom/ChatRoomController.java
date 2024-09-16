package com.wegotoo.api.chatroom;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.chatroom.request.ChatRoomCreateRequest;
import com.wegotoo.application.chatroom.ChatRoomService;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.infra.resolver.auth.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/v1/chat-rooms")
    public ApiResponse<ChatRoomResponse> createChatRoom(@RequestBody @Valid ChatRoomCreateRequest request,
                                                        @Auth Long userId) {
        return ApiResponse.ok(chatRoomService.createChatRoom(request.toService(), userId));
    }

}
