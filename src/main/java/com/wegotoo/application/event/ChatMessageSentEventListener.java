package com.wegotoo.application.event;

import com.wegotoo.application.notification.NotificationService;
import com.wegotoo.exception.NotificationSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageSentEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleChatMessageSentEvent(ChatMessageSentEvent event) throws NotificationSendException {
        notificationService.notifyChatting(event.getReceiverId(), event.getMessage());
    }

}
