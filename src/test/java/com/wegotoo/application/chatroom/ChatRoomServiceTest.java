package com.wegotoo.application.chatroom;

import static org.assertj.core.api.Assertions.assertThat;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.chatroom.request.ChatRoomCreateServiceRequest;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.Gender;
import com.wegotoo.domain.accompany.Status;
import com.wegotoo.domain.accompany.repository.AccompanyRepository;
import com.wegotoo.domain.chatroom.ChatRoom;
import com.wegotoo.domain.chatroom.repository.ChatRoomRepository;
import com.wegotoo.domain.chatroom.repository.UserChatRoomRepository;
import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRoomServiceTest extends ServiceTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UserChatRoomRepository userChatRoomRepository;

    @Autowired
    AccompanyRepository accompanyRepository;

    @Autowired
    ChatRoomService chatRoomService;

    @AfterEach
    void tearDown() {
        userChatRoomRepository.deleteAllInBatch();
        accompanyRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("채팅방을 생성한다.")
    public void createChatRoom() {
        // given
        User userA = userRepository.save(createUser("userA_guest"));
        User userB = userRepository.save(createUser("userB_admin"));
        Accompany accompany = accompanyRepository.save(createAccompany(userB));

        // when
        ChatRoomResponse result = chatRoomService.createChatRoom(createRequest(accompany.getId()), userA.getId());

        // then
        ChatRoom chatRoom = chatRoomRepository.findById(result.getId()).get();
        assertThat(chatRoom).isNotNull();
        assertThat(chatRoom.getId()).isEqualTo(result.getId());
        assertThat(chatRoom.getCode()).isEqualTo(result.getCode());
    }

    private User createUser(String username) {
        return User.builder()
                .email(username + "@email.com")
                .name(username)
                .latitude(1.1)
                .role(Role.USER)
                .profileImage(username + ".com/profile_image")
                .build();
    }

    private Accompany createAccompany(User user) {
        return Accompany.builder()
                .location("서울")
                .gender(Gender.MAN)
                .cost(200000)
                .content("같이 여행가실 분 구합니다!")
                .status(Status.RECRUIT)
                .user(user)
                .build();
    }

    private ChatRoomCreateServiceRequest createRequest(Long accompanyId) {
        return ChatRoomCreateServiceRequest.builder()
                .accompanyId(accompanyId)
                .build();
    }

}
