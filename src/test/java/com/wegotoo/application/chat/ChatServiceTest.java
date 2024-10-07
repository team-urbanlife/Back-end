package com.wegotoo.application.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.wegotoo.application.CursorResponse;
import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.chat.request.ChatSendServiceRequest;
import com.wegotoo.application.chat.response.ChatResponse;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.repository.ChatRepository;
import com.wegotoo.domain.chatroom.UserChatRoom;
import com.wegotoo.domain.chatroom.repository.UserChatRoomRepository;
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
    UserChatRoomRepository userChatRoomRepository;

    @Autowired
    ChatService chatService;

    @AfterEach
    void tearDown() {
        chatRepository.deleteAll();
        userChatRoomRepository.deleteAllInBatch();
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
        CursorResponse<String, ChatResponse> result = chatService.findAllChats(userA.getId(), 1L, null, 5);

        // then
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNextCursor()).isEqualTo(chats.get(15).getId());
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
    @DisplayName("다음 채팅 메세지 조회한다.")
    public void findAllChatsNextCursor() throws Exception {
        // given
        User userA = userRepository.save(createUser("userA"));
        User userB = userRepository.save(createUser("userB"));

        List<Chat> chats = chatRepository.saveAll(createChats(userA, userB, 1L));

        // when
        CursorResponse<String, ChatResponse> result = chatService.findAllChats(userA.getId(), 1L,
                chats.get(15).getId(), 5);

        // then
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNextCursor()).isEqualTo(chats.get(10).getId());
        assertThat(result.getContent()).hasSize(5)
                .extracting("senderId", "senderName", "message")
                .containsExactly(
                        tuple(userA.getId(), "userA", "message: 15"),
                        tuple(userB.getId(), "userB", "message: 14"),
                        tuple(userA.getId(), "userA", "message: 13"),
                        tuple(userB.getId(), "userB", "message: 12"),
                        tuple(userA.getId(), "userA", "message: 11")
                );
    }

    @Test
    @DisplayName("마지막 채팅 메세지 조회한다.")
    public void findAllChatsLastCursor() throws Exception {
        // given
        User userA = userRepository.save(createUser("userA"));
        User userB = userRepository.save(createUser("userB"));

        List<Chat> chats = chatRepository.saveAll(createChats(userA, userB, 1L));

        // when
        CursorResponse<String, ChatResponse> result = chatService.findAllChats(userA.getId(), 1L,
                chats.get(5).getId(), 5);

        // then
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getNextCursor()).isNull();
        assertThat(result.getContent()).hasSize(5)
                .extracting("senderId", "senderName", "message")
                .containsExactly(
                        tuple(userA.getId(), "userA", "message: 5"),
                        tuple(userB.getId(), "userB", "message: 4"),
                        tuple(userA.getId(), "userA", "message: 3"),
                        tuple(userB.getId(), "userB", "message: 2"),
                        tuple(userA.getId(), "userA", "message: 1")
                );
    }

//    @Test
//    @DisplayName("채팅 메세지를 전송한다.")
//    public void sendChatMessage() throws Exception {
//        // given
//        User user = userRepository.save(createUser("userA"));
//        User userB = userRepository.save(createUser("userB"));
//
//        ChatSendServiceRequest request = ChatSendServiceRequest.builder()
//                .chatRoomId(1L)
//                .message("안녕하세요 반갑습니다!")
//                .build();
//
//        // when
//        ChatResponse result = chatService.sendChatMessage(user.getId(), request);
//
//        // then
//        Chat chat = chatRepository.findAll().get(0);
//        assertThat(result.getSenderId()).isEqualTo(chat.getSenderId());
//        assertThat(result.getSenderName()).isEqualTo(user.getName());
//        assertThat(result.getSenderProfileImage()).isEqualTo(user.getProfileImage());
//        assertThat(result.getChatRoomId()).isEqualTo(chat.getChatRoomId());
//        assertThat(result.getMessage()).isEqualTo(chat.getMessage());
//    }

    @Test
    @DisplayName("인증되지 않은 사용자가 메세지를 전송할 경우 예외가 발생한다.")
    public void sendChatMessageInvalidUser() throws Exception {
        // given
        Long invalidUserId = 100L;
        User user = userRepository.save(createUser("userA"));

        ChatSendServiceRequest request = ChatSendServiceRequest.builder()
                .chatRoomId(1L)
                .message("안녕하세요 반갑습니다!")
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
