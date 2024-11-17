package com.wegotoo.application.chat.response;

import com.wegotoo.domain.chat.ChatRoomStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LastReadResponse {

    private Long chatRoomId;
    private List<LastReadMessageResponse> lastReadMessages = new ArrayList<>();

    @Builder
    private LastReadResponse(Long chatRoomId, List<LastReadMessageResponse> lastReadMessages) {
        this.chatRoomId = chatRoomId;
        this.lastReadMessages = lastReadMessages;
    }

    public static LastReadResponse of(ChatRoomStatus chatRoomStatus) {
        return LastReadResponse.builder()
                .chatRoomId(chatRoomStatus.getChatRoomId())
                .lastReadMessages(LastReadMessageResponse.toList(chatRoomStatus.getLastReadMessages()))
                .build();
    }

}
