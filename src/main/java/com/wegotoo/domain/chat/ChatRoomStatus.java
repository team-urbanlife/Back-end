package com.wegotoo.domain.chat;

import jakarta.persistence.Embedded;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "chatRoomStatus")
@NoArgsConstructor
public class ChatRoomStatus {

    @Id
    private Long chatRoomId;

    @Embedded
    private LastReads lastReads;

    @Builder
    public ChatRoomStatus(Long chatRoomId, LastReads lastReads) {
        this.chatRoomId = chatRoomId;
        this.lastReads = lastReads;
    }

    public List<LastReadMessage> getLastReadMessages() {
        return Collections.unmodifiableList(lastReads.getLastReadMessages());
    }

    public Optional<LastReadMessage> findLastReadMessageByUserId(Long userId) {
        return lastReads.findByUserId(userId);
    }

    public void updateLastChatId(Long userId, String chatId) {
        lastReads.updateLastChatId(userId, chatId);
    }

    public static ChatRoomStatus create(Long chatRoomId, LastReads lastReads) {
        return ChatRoomStatus.builder()
                .chatRoomId(chatRoomId)
                .lastReads(lastReads)
                .build();
    }

    public static ChatRoomStatus create(Long chatRoomId, Long guestId, Long adminId) {
        return ChatRoomStatus.builder()
                .chatRoomId(chatRoomId)
                .lastReads(LastReads.ofNew(guestId, adminId))
                .build();
    }

}
