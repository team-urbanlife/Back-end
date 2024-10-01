package com.wegotoo.application.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.chat.request.ChatSendServiceRequest;
import com.wegotoo.application.chat.response.ChatResponse;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.repository.ChatRepository;
import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatServiceTest extends ServiceTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    ChatService chatService;

    @AfterEach
    void tearDown() {
        chatRepository.deleteAll();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("채팅 메세지를 조회한다.")
    public void findAllChats() throws Exception {
        // given
        User userA = userRepository.save(createUser("userA"));
        User userB = userRepository.save(createUser("userB"));

        List<Chat> chats = chatRepository.saveAll(createChats(userA, userB, 1L));

        // when
        SliceResponse<ChatResponse> result = chatService.findAllChats(userA.getId(), 1L, OffsetLimit.of(1, 5));

        // then
        assertThat(result.isHasContent()).isTrue();
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isFalse();
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getContent()).hasSize(5)
                .extracting("senderId", "senderName", "message")
                .containsExactly(
                        tuple(userB.getId(), "userB", "message: 20"),
                        tuple(userA.getId(), "userA", "message: 19"),
                        tuple(userB.getId(), "userB", "message: 18"),
                        tuple(userA.getId(), "userA", "message: 17"),
                        tuple(userB.getId(), "userB", "message: 16")
                );
    }

    @Test
    @DisplayName("채팅 메세지를 전송한다.")
    public void sendChatMessage() throws Exception {
        // given
        User user = userRepository.save(createUser("userA"));

        ChatSendServiceRequest request = ChatSendServiceRequest.builder()
                .message("안녕하세요 반갑습니다!")
                .roomCode("XXXX-XXXX-XXXX-XXXX")
                .build();

        // when
        ChatResponse result = chatService.sendChatMessage(user.getId(), request);

        // then
        Chat chat = chatRepository.findAll().get(0);
        assertThat(result.getSenderId()).isEqualTo(chat.getSenderId());
        assertThat(result.getSenderName()).isEqualTo(user.getName());
        assertThat(result.getSenderProfileImage()).isEqualTo(user.getProfileImage());
        assertThat(result.getRoomCode()).isEqualTo(chat.getRoomCode());
        assertThat(result.getMessage()).isEqualTo(chat.getMessage());
    }

    @Test
    @DisplayName("인증되지 않은 사용자가 메세지를 전송할 경우 예외가 발생한다.")
    public void sendChatMessageInvalidUser() throws Exception {
        // given
        Long invalidUserId = 100L;
        User user = userRepository.save(createUser("userA"));

        ChatSendServiceRequest request = ChatSendServiceRequest.builder()
                .message("안녕하세요 반갑습니다!")
                .roomCode("XXXX-XXXX-XXXX-XXXX")
                .build();

        // when // then
        assertThatThrownBy(() -> chatService.sendChatMessage(invalidUserId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("인증되지 않은 사용자입니다.");
    }

    private User createUser(String username) {
        return User.builder()
                .email(username + "@test.com")
                .name(username)
                .latitude(1.1)
                .role(Role.USER)
                .profileImage("https://" + username + ".profile_image.com")
                .build();
    }

    private Chat createChat(User user, Long chatRoomId, Long number) {
        return Chat.builder()
                .senderId(user.getId())
                .chatRoomId(chatRoomId)
                .roomCode("0000" + chatRoomId)
                .message("message: " + number)
                .build();
    }

    private List<Chat> createChats(User userA, User userB, Long chatRoomId) {
        return LongStream.rangeClosed(1, 20)
                .mapToObj(i -> isOdd(i) ? createChat(userA, chatRoomId, i) : createChat(userB, chatRoomId, i))
                .toList();
    }

    private boolean isOdd(Long number) {
        return number % 2 == 1;
    }

}
