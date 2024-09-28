package com.wegotoo.domain.chat.repository;

import com.wegotoo.domain.chat.Chat;
import java.util.List;

public interface ChatRepositoryCustom {

    List<Chat> findLatestChatsByChatRoomIds(List<Long> chatRoomIds);

}
