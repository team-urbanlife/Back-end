package com.wegotoo.application.chatroom;

import static com.wegotoo.exception.ErrorCode.ACCOMPANY_NOT_FOUND;
import static com.wegotoo.exception.ErrorCode.CHAT_ROOM_NOT_FOUND;
import static com.wegotoo.exception.ErrorCode.USER_NOT_FOUND;
import static java.util.stream.Collectors.toMap;

import com.wegotoo.application.chatroom.request.ChatRoomCreateServiceRequest;
import com.wegotoo.application.chatroom.response.ChatRoomFindAllResponse;
import com.wegotoo.application.chatroom.response.ChatRoomFindOneResponse;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.application.chatroom.response.ChatRoomUserResponse;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.repository.AccompanyRepository;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.repository.ChatRepository;
import com.wegotoo.domain.chatroom.ChatRoom;
import com.wegotoo.domain.chatroom.UserChatRoom;
import com.wegotoo.domain.chatroom.UserChatRoomQueryEntity;
import com.wegotoo.domain.chatroom.repository.ChatRoomIds;
import com.wegotoo.domain.chatroom.repository.ChatRoomRepository;
import com.wegotoo.domain.chatroom.repository.UserChatRoomRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final AccompanyRepository accompanyRepository;

    public ChatRoomFindOneResponse findChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(CHAT_ROOM_NOT_FOUND));

        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByChatRoomIdWithUser(chatRoom.getId());

        return ChatRoomFindOneResponse.of(userChatRooms);
    }

    public List<ChatRoomFindAllResponse> findAllChatRooms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        List<UserChatRoomQueryEntity> userChatRooms = userChatRoomRepository.findAllByUserId(user.getId());
        List<Chat> chats = chatRepository.findLatestChatsByChatRoomIds(ChatRoomIds.of(userChatRooms).getChatRoomIds());

        Map<Long, UserChatRoomQueryEntity> chatMap = userChatRooms.stream()
                .collect(toMap(UserChatRoomQueryEntity::getChatRoomId, Function.identity()));

        return ChatRoomFindAllResponse.toList(chats, chatMap);
    }

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomCreateServiceRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Accompany accompany = accompanyRepository.findByIdWithUser(request.getAccompanyId())
                .orElseThrow(() -> new BusinessException(ACCOMPANY_NOT_FOUND));

        return chatRoomRepository.findByUserIdAndAccompanyId(user.getId(), accompany.getId())
                .map(chatRoom -> ChatRoomResponse.of(chatRoom.getId(), chatRoom.getCode()))
                .orElseGet(() -> {
                    ChatRoom newChatRoom = chatRoomRepository.save(ChatRoom.create());

                    userChatRoomRepository.save(UserChatRoom.ofAdmin(accompany.getUser(), newChatRoom, accompany));
                    userChatRoomRepository.save(UserChatRoom.ofGuest(user, newChatRoom, accompany));

                    return ChatRoomResponse.of(newChatRoom.getId(), newChatRoom.getCode());
                });
    }

}
