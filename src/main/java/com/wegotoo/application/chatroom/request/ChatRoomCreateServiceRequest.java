package com.wegotoo.application.chatroom.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomCreateServiceRequest {

    private Long accompanyId;

    @Builder
    private ChatRoomCreateServiceRequest(Long accompanyId) {
        this.accompanyId = accompanyId;
    }

}
