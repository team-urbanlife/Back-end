package com.wegotoo.domain.chat.repository;

import com.wegotoo.domain.chat.Chat;
import java.util.List;

public interface ChatRepositoryCustom {

    List<Chat> findAllByChatRoomId(Long chatRoomId, Integer offset, Integer limit);
    List<Chat> findLatestChatsByChatRoomIds(List<Long> chatRoomIds);

}
