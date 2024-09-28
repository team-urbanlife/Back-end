package com.wegotoo.domain.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat")
@Getter
@NoArgsConstructor
public class Chat {

    @Id
    private String id;
    private Long chatRoomId;
    private Long senderId;
    private String roomCode;
    private String message;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder
    private Chat(Long senderId, Long chatRoomId, String roomCode, String message) {
        this.senderId = senderId;
        this.chatRoomId = chatRoomId;
        this.roomCode = roomCode;
        this.message = message;
    }

}
