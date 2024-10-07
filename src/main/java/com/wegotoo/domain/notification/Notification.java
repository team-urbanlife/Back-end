package com.wegotoo.domain.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId;

    private Long chatRoomId;

    private String message;

    @Builder
    private Notification(Long id, Long receiverId, Long chatRoomId, String message) {
        this.id = id;
        this.receiverId = receiverId;
        this.chatRoomId = chatRoomId;
        this.message = message;
    }

    public static Notification create(Long receiverId,Long chatRoomId, String message) {
        return Notification.builder().
                receiverId(receiverId)
                .chatRoomId(chatRoomId)
                .message(message)
                .build();
    }

}
