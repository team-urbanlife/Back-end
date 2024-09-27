package com.wegotoo.domain.chatroom;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserChatRoomQueryEntity {

    private Long chatRoomId;
    private Long accompanyId;
    private String otherUserProfileImage;

    @QueryProjection
    public UserChatRoomQueryEntity(Long chatRoomId, Long accompanyId, String otherUserProfileImage) {
        this.chatRoomId = chatRoomId;
        this.accompanyId = accompanyId;
        this.otherUserProfileImage = otherUserProfileImage;
    }

}
