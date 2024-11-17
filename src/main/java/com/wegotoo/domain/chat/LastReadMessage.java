package com.wegotoo.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LastReadMessage {

    private Long userId;
    private String chatId;

    private static final String ACTIVE_STATUS_CHAT_ID = "-1";

    @Builder
    private LastReadMessage(Long userId, String chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }

    public static LastReadMessage of(Long userId, String chatId) {
        return LastReadMessage.builder()
                .userId(userId)
                .chatId(chatId)
                .build();
    }

    public static LastReadMessage ofNew(Long userId) {
        return LastReadMessage.builder()
                .userId(userId)
                .build();
    }

    public void updateChatId(String chatId) {
        this.chatId = chatId;
    }

    public void updateChatIdToConnected() {
        this.chatId = ACTIVE_STATUS_CHAT_ID;
    }

    public boolean isSameUser(Long userId) {
        return this.userId.equals(userId);
    }

}
