package com.wegotoo.application.chat;

import static com.wegotoo.exception.ErrorCode.NOT_VALID_USER;
import static com.wegotoo.exception.ErrorCode.USER_NOT_FOUND;

import com.wegotoo.application.OffsetLimit;
import com.wegotoo.application.SliceResponse;
import com.wegotoo.application.chat.request.ChatSendServiceRequest;
import com.wegotoo.application.chat.response.ChatResponse;
import com.wegotoo.application.event.ChatMessageSentEvent;
import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.repository.ChatRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.domain.user.Users;
import com.wegotoo.domain.user.repository.UserRepository;
import com.wegotoo.exception.BusinessException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ApplicationEventPublisher eventPublisher;

    public SliceResponse<ChatResponse> findAllChats(Long userId, Long chatRoomId, OffsetLimit offsetLimit) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        List<Chat> chats = chatRepository.findAllByChatRoomId(chatRoomId, offsetLimit.getOffset(),
                offsetLimit.getLimit());

        Set<Long> userIds = chats.stream().map(Chat::getSenderId).collect(Collectors.toSet());
        Users users = Users.of(userRepository.findAllById(userIds));

        return SliceResponse.of(ChatResponse.toList(users, chats), offsetLimit.getOffset(), offsetLimit.getLimit());
    }

    @Transactional
    public ChatResponse sendChatMessage(Long userId, ChatSendServiceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(NOT_VALID_USER));

        Chat chat = chatRepository.save(request.toDocument(user.getId()));

        eventPublisher.publishEvent(ChatMessageSentEvent.to(user.getId(), request.getMessage()));

        return ChatResponse.of(user, chat);
    }

}
