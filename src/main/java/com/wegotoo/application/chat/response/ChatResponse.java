package com.wegotoo.application.chat.response;

import static lombok.AccessLevel.PROTECTED;

import com.wegotoo.application.IdType;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.Users;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatResponse implements IdType<String> {

    private String id;
    private Long senderId;
    private Long chatRoomId;
    private String senderName;
    private String senderProfileImage;
    private String message;
    private LocalDateTime createAt;

    @Builder
    private ChatResponse(String id, Long senderId, Long chatRoomId, String senderName, String senderProfileImage, String message,
                         LocalDateTime createAt) {
        this.id = id;
        this.senderId = senderId;
        this.chatRoomId = chatRoomId;
        this.senderName = senderName;
        this.senderProfileImage = senderProfileImage;
        this.message = message;
        this.createAt = createAt;
    }

    public static ChatResponse of(User user, Chat chat) {
        return ChatResponse.builder()
                .id(chat.getId())
                .chatRoomId(chat.getChatRoomId())
                .senderId(user.getId())
                .senderName(user.getName())
                .senderProfileImage(user.getProfileImage())
                .message(chat.getMessage())
                .createAt(chat.getCreateAt())
                .build();
    }

    public static List<ChatResponse> toList(Users users, List<Chat> chats) {
        return chats.stream()
                .map(chat -> of(users.findById(chat.getSenderId()), chat))
                .toList();
    }

}
