package com.wegotoo.domain.chat.repository;

import com.wegotoo.domain.chat.Chat;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String>, ChatRepositoryCustom {

    @Aggregation(pipeline = {
            "{ $match:  { 'chatRoomId':  ?0, 'createAt':  { $lte:  ?1 } } }",
            "{ $sort:  { 'createAt':  -1 , '_id':  -1 } }",
            "{ $limit:  1}"
    })
    Optional<Chat> findLastChatByChatRoomId(Long chatRoomId, LocalDateTime disconnectTime);

}