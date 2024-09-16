package com.wegotoo.api.chatroom.request;

import com.wegotoo.application.chatroom.request.ChatRoomCreateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRequest {

    @NotNull(message = "동행 아이디 입력은 필수 입니다.")
    private Long accompanyId;

    @Builder
    private ChatRoomCreateRequest(Long accompanyId) {
        this.accompanyId = accompanyId;
    }

    public ChatRoomCreateServiceRequest toService() {
        return ChatRoomCreateServiceRequest.builder()
                .accompanyId(accompanyId)
                .build();
    }

}
