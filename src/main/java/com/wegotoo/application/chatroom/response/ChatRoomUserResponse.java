package com.wegotoo.application.chatroom.response;

import com.wegotoo.domain.chatroom.UserChatRoom;
import com.wegotoo.domain.user.User;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomUserResponse {

    private Long id;
    private String name;
    private String profileImage;

    @Builder
    private ChatRoomUserResponse(Long id, String name, String profileImage) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
    }

    public static ChatRoomUserResponse of(User user) {
        return ChatRoomUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .build();
    }

    public static List<ChatRoomUserResponse> toList(List<UserChatRoom> userChatRooms) {
        return userChatRooms.stream()
                .map(userChatRoom -> ChatRoomUserResponse.of(userChatRoom.getUser()))
                .toList();
    }

}
