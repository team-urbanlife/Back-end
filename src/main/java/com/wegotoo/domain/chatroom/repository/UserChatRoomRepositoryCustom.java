package com.wegotoo.domain.chatroom.repository;

import com.wegotoo.domain.chatroom.UserChatRoomQueryEntity;
import java.util.List;

public interface UserChatRoomRepositoryCustom {

    List<UserChatRoomQueryEntity> findAllByUserId(Long userId);

}
