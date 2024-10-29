package com.wegotoo.domain.chatroom.repository;

import static com.wegotoo.domain.accompany.Gender.NO_MATTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.wegotoo.domain.DataJpaTestSupport;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.repository.AccompanyRepository;
import com.wegotoo.domain.chatroom.ChatRoom;
import com.wegotoo.domain.chatroom.UserChatRoom;
import com.wegotoo.domain.chatroom.UserChatRoomQueryEntity;
import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserChatRoomRepositoryImplTest extends DataJpaTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccompanyRepository accompanyRepository;

    @Autowired
    UserChatRoomRepository userChatRoomRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    private final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
    private final LocalDate END_DATE = LocalDate.of(2020, 12, 31);


    @AfterEach
    void tearDown() {
        userChatRoomRepository.deleteAllInBatch();
        accompanyRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("사용자 채팅 조회 테스트")
    public void findAllByUserId() throws Exception {
        // given
        User userA = userRepository.save(createUser("userA"));
        User userB = userRepository.save(createUser("userB"));
        User userC = userRepository.save(createUser("userC"));
        User userD = userRepository.save(createUser("userD"));
        User userE = userRepository.save(createUser("userE"));

        Accompany accompany = accompanyRepository.save(createAccompany(userA));

        ChatRoom chatRoomA = chatRoomRepository.save(createChatRoom("0000A"));
        ChatRoom chatRoomB = chatRoomRepository.save(createChatRoom("0000A"));
        ChatRoom chatRoomC = chatRoomRepository.save(createChatRoom("0000A"));
        ChatRoom chatRoomD = chatRoomRepository.save(createChatRoom("0000A"));

        userChatRoomRepository.saveAll(createUserChatRooms(userA, userB, chatRoomA, accompany));
        userChatRoomRepository.saveAll(createUserChatRooms(userA, userC, chatRoomB, accompany));
        userChatRoomRepository.saveAll(createUserChatRooms(userA, userD, chatRoomC, accompany));
        userChatRoomRepository.saveAll(createUserChatRooms(userA, userE, chatRoomD, accompany));

        // when
        List<UserChatRoomQueryEntity> result = userChatRoomRepository.findAllByUserId(userA.getId());

        // thee
        assertThat(result).hasSize(4)
                .extracting("chatRoomId", "accompanyId", "otherUserProfileImage")
                .containsExactly(
                        tuple(1L, accompany.getId(), "userB.image.com"),
                        tuple(2L, accompany.getId(), "userC.image.com"),
                        tuple(3L, accompany.getId(), "userD.image.com"),
                        tuple(4L, accompany.getId(), "userE.image.com")
                );
    }

    private User createUser(String username) {
        return User.builder()
                .name(username)
                .email(username + "@example.com")
                .profileImage(username + ".image.com")
                .role(Role.USER)
                .build();
    }

    private Accompany createAccompany(User user) {
        return Accompany.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .title(user.getName() + "과 제주도 여행 모집")
                .location("제주도")
                .latitude(0.0)
                .longitude(0.0)
                .personnel(3)
                .gender(NO_MATTER)
                .startAge(20)
                .endAge(29)
                .cost(1000000)
                .content("여행 관련 글")
                .user(user)
                .build();
    }

    private ChatRoom createChatRoom(String roomCode) {
        return ChatRoom.builder()
                .code(roomCode)
                .build();
    }

    private List<UserChatRoom> createUserChatRooms(User adminUser, User guestUser, ChatRoom chatRoom,
                                                   Accompany accompany) {
        return List.of(UserChatRoom.ofAdmin(adminUser, chatRoom, accompany),
                UserChatRoom.ofGuest(guestUser, chatRoom, accompany));
    }

}
