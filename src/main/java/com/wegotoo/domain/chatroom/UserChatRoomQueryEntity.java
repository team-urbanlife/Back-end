package com.wegotoo.domain.chatroom;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserChatRoomQueryEntity {

    private Long chatRoomId;
    private Long accompanyId;
    private String accompanyTitle;
    private String otherUserProfileImage;

    @QueryProjection
    public UserChatRoomQueryEntity(Long chatRoomId, Long accompanyId, String accompanyTitle,
                                   String otherUserProfileImage) {
        this.chatRoomId = chatRoomId;
        this.accompanyId = accompanyId;
        this.accompanyTitle = accompanyTitle;
        this.otherUserProfileImage = otherUserProfileImage;
    }

}
