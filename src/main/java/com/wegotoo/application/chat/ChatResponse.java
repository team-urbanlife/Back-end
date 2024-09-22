package com.wegotoo.application.chat;

import static lombok.AccessLevel.PROTECTED;

import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.user.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatResponse {

    private Long senderId;
    private String senderName;
    private String senderProfileImage;
    private String roomCode;
    private String message;
    private LocalDateTime createAt;

    @Builder
    private ChatResponse(Long senderId, String senderName, String senderProfileImage, String roomCode, String message,
                         LocalDateTime createAt) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderProfileImage = senderProfileImage;
        this.roomCode = roomCode;
        this.message = message;
        this.createAt = createAt;
    }

    public static ChatResponse of(User user, Chat chat) {
        return ChatResponse.builder()
                .senderId(user.getId())
                .senderName(user.getName())
                .senderProfileImage(user.getProfileImage())
                .roomCode(chat.getRoomCode())
                .message(chat.getMessage())
                .createAt(chat.getCreateAt())
                .build();
    }

}
