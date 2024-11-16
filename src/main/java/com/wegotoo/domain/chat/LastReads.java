package com.wegotoo.domain.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LastReads {

    private List<LastReadMessage> lastReadMessages = new ArrayList<>();

    @Builder
    private LastReads(List<LastReadMessage> lastReadMessages) {
        this.lastReadMessages = lastReadMessages;
    }

    public void updateLastChatId(Long userId, String chatId) {
        lastReadMessages.stream()
                .filter(lastRead -> lastRead.isSameUser(userId))
                .findFirst()
                .ifPresent(lastRead -> lastRead.updateChatId(chatId));
    }

    public Optional<LastReadMessage> findByUserId(Long userId) {
        return lastReadMessages.stream()
                .filter(lastRead -> lastRead.isSameUser(userId))
                .findFirst();
    }

    public static LastReads of(List<LastReadMessage> lastReadMessages) {
        return LastReads.builder()
                .lastReadMessages(lastReadMessages)
                .build();
    }

    public static LastReads ofNew(Long guestId, Long adminId) {
        return LastReads.builder()
                .lastReadMessages(List.of(
                        LastReadMessage.ofNew(guestId),
                        LastReadMessage.ofNew(adminId)))
                .build();
    }

}
