package com.wegotoo.application.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.ChatRoomStatus;
import com.wegotoo.domain.chat.LastReadMessage;
import com.wegotoo.domain.chat.LastReads;
import com.wegotoo.domain.chat.OnlineUser;
import com.wegotoo.domain.chat.repository.ChatRepository;
import com.wegotoo.domain.chat.repository.ChatRoomStatusRepository;
import com.wegotoo.domain.chat.repository.OnlineUserRepository;
import com.wegotoo.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class ChatUserStatusServiceTest extends ServiceTestSupport {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatRoomStatusRepository chatRoomStatusRepository;

    @Autowired
    private OnlineUserRepository onlineUserRepository;

    @Autowired
    private ChatUserStatusService chatUserStatusService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @AfterEach
    public void tearDown() {
        onlineUserRepository.deleteAll();
        chatRepository.deleteAll();
        chatRoomStatusRepository.deleteAll();
    }

    @Test
    @DisplayName("유저가 접속한다.")
    public void addOnlineUser() {
        // given
        Long userId = 1L;
        Long chatRoomId = 100L;

        LastReadMessage lastReadMessage = LastReadMessage.of(userId, null);
        LastReads lastReads = LastReads.of(List.of(lastReadMessage));

        ChatRoomStatus chatRoomStatus = chatRoomStatusRepository.save(ChatRoomStatus.create(chatRoomId, lastReads));

        // when
        chatUserStatusService.addOnlineUser(userId, chatRoomStatus.getChatRoomId());

        // then
        Optional<OnlineUser> findOnlineUser = onlineUserRepository.findById(userId);
        Optional<ChatRoomStatus> findChatRoomStatus = chatRoomStatusRepository.findById(chatRoomId);

        assertThat(findOnlineUser).isNotEmpty();
        assertThat(findChatRoomStatus).isNotEmpty();

        assertThat(findOnlineUser.get().getUserId()).isEqualTo(userId);
        assertThat(findOnlineUser.get().getChatRoomId()).isEqualTo(chatRoomId);

        assertThat(findChatRoomStatus.get().getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(findChatRoomStatus.get().getLastReadMessages())
                .extracting("userId", "chatId")
                .containsExactly(
                        tuple(1L, "-1")
                );
    }

    @Test
    @DisplayName("유저가 접속을 해제한다.")
    public void removeOnlineUser() {
        // given
        Long userId = 1L;
        Long chatRoomId = 100L;

        LastReadMessage lastReadMessage = LastReadMessage.of(userId, null);
        LastReads lastReads = LastReads.of(List.of(lastReadMessage));

        OnlineUser onlineUser = onlineUserRepository.save(OnlineUser.create(userId, chatRoomId));
        ChatRoomStatus chatRoomStatus = chatRoomStatusRepository.save(ChatRoomStatus.create(chatRoomId, lastReads));

        // when
        chatUserStatusService.removeOnlineUser(onlineUser.getUserId(), LocalDateTime.now());

        // then
        Optional<OnlineUser> findUser = onlineUserRepository.findById(onlineUser.getUserId());
        Optional<ChatRoomStatus> findChatRoomStatus
                = chatRoomStatusRepository.findById(chatRoomStatus.getChatRoomId());

        assertThat(findUser).isEmpty();
        assertThat(findChatRoomStatus).isNotEmpty();

        assertThat(findChatRoomStatus.get().getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(findChatRoomStatus.get().getLastReadMessages())
                .extracting("userId", "chatId")
                .containsExactly(
                        tuple(1L, null)
                );
    }

    @Test
    @DisplayName("유저 접속 해제 시 마지막 채팅 정보를 업데이트한다.")
    public void removeOnlineUserUpdateLastRead() {
        // given
        Long userId = 1L;
        Long chatRoomId = 100L;

        LastReadMessage lastReadMessage = LastReadMessage.of(userId, "-1");
        LastReads lastReads = LastReads.of(List.of(lastReadMessage));

        OnlineUser onlineUser = onlineUserRepository.save(OnlineUser.create(userId, chatRoomId));
        ChatRoomStatus chatRoomStatus = chatRoomStatusRepository.save(ChatRoomStatus.create(chatRoomId, lastReads));

        List<Chat> chats = chatRepository.saveAll(createChats(userId, chatRoomId));

        // when
        chatUserStatusService.removeOnlineUser(onlineUser.getUserId(), LocalDateTime.now());

        // then
        Chat lastChat = chats.get(chats.size() - 1);
        Optional<OnlineUser> findUser = onlineUserRepository.findById(onlineUser.getUserId());
        Optional<ChatRoomStatus> findChatRoomStatus
                = chatRoomStatusRepository.findById(chatRoomStatus.getChatRoomId());

        assertThat(findUser).isEmpty();
        assertThat(findChatRoomStatus).isNotEmpty();

        assertThat(findChatRoomStatus.get().getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(findChatRoomStatus.get().getLastReadMessages())
                .extracting("userId", "chatId")
                .containsExactly(
                        tuple(1L, lastChat.getId())
                );
    }

    @Test
    @DisplayName("유저 접속 해제 시 사용자 정보가 존재하지 않으면 오류가 발생한다.")
    public void removeOnlineUserWithoutUser() {
        // given
        Long invalidUserId = 1L;

        // when // then
        assertThatThrownBy(() -> chatUserStatusService.removeOnlineUser(invalidUserId, LocalDateTime.now()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("유저 접속 해제 시 채팅방 상태 정보가 존재하지 않으면 오류가 발생한다.")
    public void removeOnlineUserWithoutChatRoomStatus() {
        // given
        Long userId = 1L;
        Long chatRoomId = 100L;

        OnlineUser onlineUser = onlineUserRepository.save(OnlineUser.create(userId, chatRoomId));

        // when // then
        assertThatThrownBy(() -> chatUserStatusService.removeOnlineUser(onlineUser.getUserId(), LocalDateTime.now()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("채팅방을 찾을 수 없습니다.");
    }


    private List<Chat> createChats(Long userId, Long chatRoomId) {
        return LongStream.rangeClosed(1, 10)
                .mapToObj(i -> Chat.builder()
                        .chatRoomId(chatRoomId)
                        .senderId(userId)
                        .roomCode("XXX-XXX-XXX-XXX")
                        .message(i + " Message!")
                        .build())
                .toList();
    }

}
