package com.wegotoo.application.chatroom.response;

import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chatroom.UserChatRoomQueryEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomFindAllResponse {

    private Long chatRoomId;
    private Long accompanyId;
    private String otherUserProfileImage;
    private String lastMessage;
    private LocalDateTime lastMessageCreateAt;

    @Builder
    private ChatRoomFindAllResponse(Long chatRoomId, Long accompanyId, String otherUserProfileImage, String lastMessage,
                                    LocalDateTime lastMessageCreateAt) {
        this.chatRoomId = chatRoomId;
        this.accompanyId = accompanyId;
        this.otherUserProfileImage = otherUserProfileImage;
        this.lastMessage = lastMessage;
        this.lastMessageCreateAt = lastMessageCreateAt;
    }

    public static List<ChatRoomFindAllResponse> toList(List<Chat> chats,
                                                       Map<Long, UserChatRoomQueryEntity> chatRoomMap) {
        return chats.stream()
                .map(chat -> ChatRoomFindAllResponse.of(chat, chatRoomMap.get(chat.getChatRoomId())))
                .toList();
    }

    public static ChatRoomFindAllResponse of(Chat chat, UserChatRoomQueryEntity userChatRoom) {
        return ChatRoomFindAllResponse.builder()
                .chatRoomId(userChatRoom.getChatRoomId())
                .accompanyId(userChatRoom.getAccompanyId())
                .otherUserProfileImage(userChatRoom.getOtherUserProfileImage())
                .lastMessage(chat.getMessage())
                .lastMessageCreateAt(chat.getCreateAt())
                .build();
    }

}
