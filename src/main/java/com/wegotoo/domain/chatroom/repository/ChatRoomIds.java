package com.wegotoo.domain.chatroom.repository;

import com.wegotoo.domain.chatroom.UserChatRoomQueryEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomIds {

    private List<Long> chatRoomIds = new ArrayList<>();

    @Builder
    private ChatRoomIds(List<Long> chatRoomIds) {
        this.chatRoomIds = chatRoomIds;
    }

    public static ChatRoomIds of(List<UserChatRoomQueryEntity> userChatRoomEntities) {
        return ChatRoomIds.builder()
                .chatRoomIds(userChatRoomEntities.stream()
                        .map(UserChatRoomQueryEntity::getChatRoomId)
                        .toList())
                .build();
    }

}
