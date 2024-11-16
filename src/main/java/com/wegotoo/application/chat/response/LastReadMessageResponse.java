package com.wegotoo.application.chat.response;

import com.wegotoo.domain.chat.LastReadMessage;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LastReadMessageResponse {

    private Long userId;
    private String chatId;

    @Builder
    private LastReadMessageResponse(Long userId, String chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }

    public static LastReadMessageResponse of(LastReadMessage lastReadMessage) {
        return LastReadMessageResponse.builder()
                .userId(lastReadMessage.getUserId())
                .chatId(lastReadMessage.getChatId())
                .build();
    }

    public static List<LastReadMessageResponse> toList(List<LastReadMessage> lastReadMessages) {
        return lastReadMessages.stream()
                .map(LastReadMessageResponse::of)
                .toList();
    }

}
