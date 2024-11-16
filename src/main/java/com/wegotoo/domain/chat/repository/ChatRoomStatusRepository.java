package com.wegotoo.domain.chat.repository;

import com.wegotoo.domain.chat.ChatRoomStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomStatusRepository extends MongoRepository<ChatRoomStatus, Long> {
}
