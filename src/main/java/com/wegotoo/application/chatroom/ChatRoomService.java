package com.wegotoo.application.chatroom;

import static com.wegotoo.exception.ErrorCode.NOT_FOUND_ACCOMPANY;
import static com.wegotoo.exception.ErrorCode.USER_NOT_FOUND;

import com.wegotoo.application.chatroom.request.ChatRoomCreateServiceRequest;
import com.wegotoo.application.chatroom.response.ChatRoomResponse;
import com.wegotoo.domain.accompany.Accompany;
import com.wegotoo.domain.accompany.repository.AccompanyRepository;
import com.wegotoo.domain.chatroom.ChatRoom;
import com.wegotoo.domain.chatroom.UserChatRoom;
import com.wegotoo.domain.chatroom.repository.ChatRoomRepository;
import com.wegotoo.domain.chatroom.repository.UserChatRoomRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final AccompanyRepository accompanyRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomCreateServiceRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Accompany accompany = accompanyRepository.findByIdWithUser(request.getAccompanyId())
                .orElseThrow(() -> new BusinessException(NOT_FOUND_ACCOMPANY));

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
