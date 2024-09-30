package com.wegotoo.api.chatroom;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.api.chatroom.request.ChatRoomCreateRequest;
import com.wegotoo.application.chatroom.ChatRoomService;
import com.wegotoo.application.chatroom.response.ChatRoomFindAllResponse;
import com.wegotoo.application.chatroom.response.ChatRoomFindOneResponse;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.infra.resolver.auth.Auth;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/v1/chat-rooms/{chatRoomId}")
    public ApiResponse<ChatRoomFindOneResponse> findChatRoom(@Auth Long userId,
                                                             @PathVariable(name = "chatRoomId") Long chatRoomId) {
        return ApiResponse.ok(chatRoomService.findChatRoom(userId, chatRoomId));
    }

    @GetMapping("/v1/chat-rooms")
    public ApiResponse<List<ChatRoomFindAllResponse>> findChatRooms(@Auth Long userId) {
        return ApiResponse.ok(chatRoomService.findAllChatRooms(userId));
    }

    @PostMapping("/v1/chat-rooms")
    public ApiResponse<ChatRoomResponse> createChatRoom(@RequestBody @Valid ChatRoomCreateRequest request,
                                                        @Auth Long userId) {
        return ApiResponse.ok(chatRoomService.createChatRoom(request.toService(), userId));
    }

}
