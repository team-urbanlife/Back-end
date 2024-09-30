package com.wegotoo.application.chatroom.response;

import com.wegotoo.domain.chatroom.UserChatRoom;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomFindOneResponse {

    private List<ChatRoomUserResponse> users = new ArrayList<>();

    @Builder
    private ChatRoomFindOneResponse(List<ChatRoomUserResponse> users) {
        this.users = users;
    }

    public static ChatRoomFindOneResponse of(List<UserChatRoom> userChatRooms) {
        return ChatRoomFindOneResponse.builder()
                .users(ChatRoomUserResponse.toList(userChatRooms))
                .build();
    }

}
