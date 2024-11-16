package com.wegotoo.application.chat;

import static com.wegotoo.exception.ErrorCode.CHAT_ROOM_NOT_FOUND;
import static com.wegotoo.exception.ErrorCode.USER_NOT_FOUND;

import com.wegotoo.domain.chat.Chat;
import com.wegotoo.domain.chat.ChatRoomStatus;
import com.wegotoo.domain.chat.LastReadMessage;
import com.wegotoo.domain.chat.OnlineUser;
import com.wegotoo.domain.chat.repository.ChatRepository;
import com.wegotoo.domain.chat.repository.ChatRoomStatusRepository;
import com.wegotoo.domain.chat.repository.OnlineUserRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.infra.socket.WebSocketResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatUserStatusService {

    private final ChatRepository chatRepository;
    private final ChatRoomStatusRepository chatRoomStatusRepository;
    private final OnlineUserRepository onlineUserRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String PREFIX_CHAT_ROOM_STATUS_DESTINATION = "/topic/chat-rooms/";
    private static final String SUFFIX_CHAT_ROOM_STATUS_DESTINATION = "/status";

    public void addOnlineUser(Long userId, Long chatRoomId) {
        ChatRoomStatus chatRoomStatus = chatRoomStatusRepository.findById(chatRoomId)
                .orElseThrow(() -> new BusinessException(CHAT_ROOM_NOT_FOUND));

        LastReadMessage oldLastReadMessage = chatRoomStatus.findLastReadMessageByUserId(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        String oldLastReadChatId = oldLastReadMessage.getChatId();
        oldLastReadMessage.updateChatIdToConnected();

        onlineUserRepository.save(OnlineUser.create(userId, chatRoomId));
        chatRoomStatusRepository.save(chatRoomStatus);

        messagingTemplate.convertAndSend(createDestination(chatRoomId),
                WebSocketResponse.ofEnterUser(LastReadMessage.of(userId, oldLastReadChatId)));
    }

    public void removeOnlineUser(Long userId, LocalDateTime disconnectTime) {
        OnlineUser onlineUser = onlineUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        ChatRoomStatus chatRoomStatus = chatRoomStatusRepository.findById(onlineUser.getChatRoomId())
                .orElseThrow(() -> new BusinessException(CHAT_ROOM_NOT_FOUND));

        String lastChatId = chatRepository.findLastChatByChatRoomId(onlineUser.getChatRoomId(), disconnectTime)
                .map(Chat::getId)
                .orElse(null);

        chatRoomStatus.updateLastChatId(userId, lastChatId);

        onlineUserRepository.deleteById(userId);
        chatRoomStatusRepository.save(chatRoomStatus);

        messagingTemplate.convertAndSend(createDestination(onlineUser.getChatRoomId()),
                WebSocketResponse.ofLeaveUser(LastReadMessage.of(userId, lastChatId)));
    }

    private static String createDestination(Long chatRoomId) {
        return PREFIX_CHAT_ROOM_STATUS_DESTINATION + chatRoomId + SUFFIX_CHAT_ROOM_STATUS_DESTINATION;
    }

}
