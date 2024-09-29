package com.wegotoo.application.chatroom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.wegotoo.application.ServiceTestSupport;
import com.wegotoo.application.chatroom.request.ChatRoomCreateServiceRequest;
import com.wegotoo.application.chatroom.response.ChatRoomFindAllResponse;
import com.wegotoo.application.chatroom.response.ChatRoomFindOneResponse;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.Gender;
import com.wegotoo.domain.accompany.Status;
import com.wegotoo.domain.accompany.repository.AccompanyRepository;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.repository.ChatRepository;
import com.wegotoo.domain.chatroom.ChatRoom;
import com.wegotoo.domain.chatroom.UserChatRoom;
import com.wegotoo.domain.chatroom.repository.ChatRoomRepository;
import com.wegotoo.domain.chatroom.repository.UserChatRoomRepository;
import com.wegotoo.domain.user.Role;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRoomServiceTest extends ServiceTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

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
        chatRepository.deleteAll();
        userChatRoomRepository.deleteAllInBatch();
        accompanyRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("채팅방을 단건 조회한다.")
    public void findChatRoom() {
        // given
        User userA = userRepository.save(createUser("userA"));
        User userB = userRepository.save(createUser("userB"));
        Accompany accompany = accompanyRepository.save(createAccompany(userA));
        ChatRoom chatRoom = chatRoomRepository.save(createChatRoomWithCode("0000A"));

        userChatRoomRepository.saveAll(createUserChatRooms(userA, userB, chatRoom, accompany));

        // when
        ChatRoomFindOneResponse result = chatRoomService.findChatRoom(userA.getId(), chatRoom.getId());

        // then
        assertThat(result.getUsers()).hasSize(2)
                .extracting("id", "name", "profileImage")
                .containsExactly(
                        tuple(userA.getId(), userA.getName(), userA.getProfileImage()),
                        tuple(userB.getId(), userB.getName(), userB.getProfileImage())
                );
    }

    @Test
    @DisplayName("채팅방을 전체 조회한다.")
    public void findAllChatRooms() {
        // given
        User userA = userRepository.save(createUser("userA"));
        User userB = userRepository.save(createUser("userB"));
        User userC = userRepository.save(createUser("userC"));
        User userD = userRepository.save(createUser("userD"));
        User userE = userRepository.save(createUser("userE"));

        Accompany accompany = accompanyRepository.save(createAccompany(userA));

        ChatRoom chatRoomA = chatRoomRepository.save(createChatRoomWithCode("0000A"));
        ChatRoom chatRoomB = chatRoomRepository.save(createChatRoomWithCode("0000B"));
        ChatRoom chatRoomC = chatRoomRepository.save(createChatRoomWithCode("0000C"));
        ChatRoom chatRoomD = chatRoomRepository.save(createChatRoomWithCode("0000D"));

        userChatRoomRepository.saveAll(createUserChatRooms(userA, userB, chatRoomA, accompany));
        userChatRoomRepository.saveAll(createUserChatRooms(userA, userC, chatRoomB, accompany));
        userChatRoomRepository.saveAll(createUserChatRooms(userA, userD, chatRoomC, accompany));
        userChatRoomRepository.saveAll(createUserChatRooms(userA, userE, chatRoomD, accompany));

        List<Chat> chatsA = chatRepository.saveAll(createChats(userA, userB, chatRoomA));
        List<Chat> chatsB = chatRepository.saveAll(createChats(userA, userC, chatRoomB));
        List<Chat> chatsC = chatRepository.saveAll(createChats(userA, userD, chatRoomC));
        List<Chat> chatsD = chatRepository.saveAll(createChats(userA, userE, chatRoomD));

        // when
        List<ChatRoomFindAllResponse> result = chatRoomService.findAllChatRooms(userA.getId());

        // then
        assertThat(result).hasSize(4)
                .extracting("chatRoomId", "otherUserProfileImage", "lastMessage")
                .containsExactly(
                        tuple(chatRoomD.getId(), "profile_image.com/userE", "message20"),
                        tuple(chatRoomC.getId(), "profile_image.com/userD", "message20"),
                        tuple(chatRoomB.getId(), "profile_image.com/userC", "message20"),
                        tuple(chatRoomA.getId(), "profile_image.com/userB", "message20")
                );
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

    @Test
    @DisplayName("채팅방 생성 시 동행 게시글에 대한 채팅방이 존재하면 기존 채팅방을 반환한다.")
    public void createChatRoomAlreadyExistsForAccompany() {
        // given
        User userA = userRepository.save(createUser("userA_guest"));
        User userB = userRepository.save(createUser("userB_admin"));

        Accompany accompany = accompanyRepository.save(createAccompany(userB));
        ChatRoom chatRoom = chatRoomRepository.save(createChatRoomWithCode("000-000-000-000"));

        userChatRoomRepository.saveAll(List.of(UserChatRoom.ofGuest(userA, chatRoom, accompany),
                UserChatRoom.ofAdmin(userB, chatRoom, accompany)));

        // when
        ChatRoomResponse result = chatRoomService.createChatRoom(createRequest(accompany.getId()), userA.getId());

        // then
        assertThat(result.getId()).isEqualTo(chatRoom.getId());
        assertThat(result.getCode()).isEqualTo(chatRoom.getCode());
    }

    private User createUser(String username) {
        return User.builder()
                .name(username)
                .email(username + "@email.com")
                .latitude(1.1)
                .role(Role.USER)
                .profileImage("profile_image.com/" + username)
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

    private ChatRoom createChatRoomWithCode(String code) {
        return ChatRoom.builder()
                .code(code)
                .build();
    }

    private UserChatRoom createUserChatRoom(User user, ChatRoom chatRoom, Accompany accompany,
                                            com.wegotoo.domain.chatroom.Role role) {
        return UserChatRoom.builder()
                .user(user)
                .chatRoom(chatRoom)
                .accompany(accompany)
                .role(role)
                .build();
    }

    private List<UserChatRoom> createUserChatRooms(User admin, User guest, ChatRoom chatRoom, Accompany accompany) {
        return List.of(createUserChatRoom(admin, chatRoom, accompany, com.wegotoo.domain.chatroom.Role.ADMIN),
                createUserChatRoom(guest, chatRoom, accompany, com.wegotoo.domain.chatroom.Role.GUEST));
    }

    private Chat createChat(User user, ChatRoom chatRoom, Long messageNumber) {
        return Chat.builder()
                .chatRoomId(chatRoom.getId())
                .senderId(user.getId())
                .roomCode(chatRoom.getCode())
                .message("message" + messageNumber)
                .build();
    }

    private List<Chat> createChats(User admin, User guest, ChatRoom chatRoom) {
        return LongStream.rangeClosed(1, 20)
                .mapToObj(i -> isOdd(i) ? createChat(admin, chatRoom, i) : createChat(guest, chatRoom, i))
                .toList();
    }

    private boolean isOdd(Long number) {
        return number % 2 == 1;
    }

    private ChatRoomCreateServiceRequest createRequest(Long accompanyId) {
        return ChatRoomCreateServiceRequest.builder()
                .accompanyId(accompanyId)
                .build();
    }

}
