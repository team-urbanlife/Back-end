package com.wegotoo.domain.chatroom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "chat_room_code")
    private String code;

    @Builder
    private ChatRoom(String code) {
        this.code = code;
    }

    public static ChatRoom create() {
        return ChatRoom.builder()
                .code(UUID.randomUUID().toString())
                .build();
    }

}
