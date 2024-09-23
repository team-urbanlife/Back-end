package com.wegotoo.application.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.chat.request.ChatSendServiceRequest;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.repository.ChatRepository;
import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
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

}
