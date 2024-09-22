package com.wegotoo.domain.chat.repository;

import com.wegotoo.domain.chat.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
}