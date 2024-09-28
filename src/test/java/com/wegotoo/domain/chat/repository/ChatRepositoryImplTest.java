package com.wegotoo.domain.chat.repository;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.groups.Tuple.tuple;

import com.wegotoo.config.EmbeddedMongoConfig;
import com.wegotoo.config.MongoConfig;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.repository.ChatRepository;
import java.util.List;
import java.util.stream.LongStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

@DataMongoTest
@Import({MongoConfig.class, EmbeddedMongoConfig.class})
public class ChatRepositoryImplTest {

    @Autowired
    ChatRepository chatRepository;

    @AfterEach
    void tearDown() {
        chatRepository.deleteAll();
    }

    @Test
    @DisplayName("마지막 채팅 조회")
    public void findLastChatsByChatRoomIds() {
        // given
        List<Chat> chatsWithUser1ToUser2 = chatRepository.saveAll(createChatsWithUsers(1L, 2L, 1L));
        List<Chat> chatsWithUser1ToUser3 = chatRepository.saveAll(createChatsWithUsers(1L, 3L, 2L));
        List<Chat> chatsWithUser1ToUser4 = chatRepository.saveAll(createChatsWithUsers(1L, 4L, 3L));
        List<Chat> chatsWithUser1ToUser5 = chatRepository.saveAll(createChatsWithUsers(1L, 5L, 4L));

        // when
        List<Chat> result = chatRepository.findLatestChatsByChatRoomIds(List.of(1L, 2L, 3L, 4L));

        // then
        assertThat(result).hasSize(4)
                .extracting("chatRoomId", "senderId", "roomCode", "message")
                .containsExactly(
                        tuple(4L, 5L, "00004", "message: 20"),
                        tuple(3L, 4L, "00003", "message: 20"),
                        tuple(2L, 3L, "00002", "message: 20"),
                        tuple(1L, 2L, "00001", "message: 20")
                );
    }

    @Test
    @DisplayName("마지막 채팅 조회 시 채팅이 존재하지 않으면 빈 리스트를 반환")
    public void findLastChatsWithoutChatRoomIds() {
        // given
        List<Chat> chatsWithUser1ToUser2 = chatRepository.saveAll(createChatsWithUsers(1L, 2L, 1L));
        List<Chat> chatsWithUser1ToUser3 = chatRepository.saveAll(createChatsWithUsers(1L, 3L, 2L));
        List<Chat> chatsWithUser1ToUser4 = chatRepository.saveAll(createChatsWithUsers(1L, 4L, 3L));
        List<Chat> chatsWithUser1ToUser5 = chatRepository.saveAll(createChatsWithUsers(1L, 5L, 4L));

        // when
        List<Chat> result = chatRepository.findLatestChatsByChatRoomIds(List.of());

        assertThat(result).isEmpty();
    }

    public List<Chat> createChatsWithUsers(Long adminId, Long guestId, Long chatRoomId) {
        return LongStream.rangeClosed(1, 20)
                .mapToObj(i -> isOdd(i) ? createChat(adminId, chatRoomId, i) : createChat(guestId, chatRoomId, i))
                .toList();
    }

    public Chat createChat(Long senderId, Long chatRoomId, Long message) {
        return Chat.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .roomCode("0000" + chatRoomId)
                .message("message: " + message)
                .build();
    }

    private boolean isOdd(Long number) {
        return number % 2 == 1;
    }

}
