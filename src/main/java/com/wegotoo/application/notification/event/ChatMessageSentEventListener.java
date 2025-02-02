package com.wegotoo.application.notification.event;

import static com.wegotoo.exception.ErrorCode.USER_NOT_FOUND;

import com.wegotoo.application.notification.NotificationService;
import com.wegotoo.application.notification.request.SendRequest;
import com.wegotoo.domain.chatroom.UserChatRoom;
import com.wegotoo.domain.chatroom.repository.UserChatRoomRepository;
import com.wegotoo.domain.user.User;
import com.wegotoo.exception.BusinessException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageSentEventListener {

    private final NotificationService notificationService;
    private final UserChatRoomRepository userChatRoomRepository;

    @EventListener
    public void handleChatMessageSentEvent(ChatMessageSentEvent event) throws IOException {
        notificationService.notifyChatting(
                SendRequest.of(otherUser(event.getChatRoomId(), event.getSendUserId()),
                        event.getChatRoomId(), event.getMessage())
        );
    }

    public Long otherUser(Long chatRoomId, Long sendUserId) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByChatRoomIdWithUser(chatRoomId);

        User otherUser = userChatRooms.stream()
                .map(UserChatRoom::getUser)
                .filter(chatUser -> !chatUser.getId().equals(sendUserId))
                .findFirst().orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        return otherUser.getId();
    }

}
