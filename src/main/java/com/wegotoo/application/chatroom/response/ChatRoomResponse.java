package com.wegotoo.application.chatroom.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponse {

    private Long id;
    private String code;

    @Builder
    private ChatRoomResponse(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public static ChatRoomResponse of(Long id, String code) {
        return ChatRoomResponse.builder()
                .id(id)
                .code(code)
                .build();
    }

}
