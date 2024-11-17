package com.wegotoo.infra.socket;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.getDestination;
import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.getUser;

import com.wegotoo.application.chat.ChatUserStatusService;
import com.wegotoo.infra.security.user.CustomUserDetails;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {

    private final ChatUserStatusService chatUserStatusService;
    private static final String PREFIX_CHAT_ROOM_STATUS_DESTINATION = "/topic/chat-rooms/";
    private static final String SUFFIX_CHAT_ROOM_STATUS_DESTINATION = "/status";

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        MessageHeaders messageHeaders = event.getMessage().getHeaders();
        String destination = getDestination(messageHeaders);

        if (!isChatRoomStatusDestination(destination)) {
            return;
        }

        Long userId = extractUserId(getUser(messageHeaders));
        Long chatRoomId = extractChatRoomId(destination);

        chatUserStatusService.addOnlineUser(userId, chatRoomId);
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        MessageHeaders messageHeaders = event.getMessage().getHeaders();
        Principal principal = getUser(messageHeaders);

        if (!hasPrincipal(principal)) {
            return;
        }

        Long userId = extractUserId(principal);
        LocalDateTime disconnectTime = convertTimestampToLocalDateTime(event.getTimestamp());

        chatUserStatusService.removeOnlineUser(userId, disconnectTime);
    }

    private boolean isChatRoomStatusDestination(String destination) {
        return destination.startsWith(PREFIX_CHAT_ROOM_STATUS_DESTINATION) &&
                destination.endsWith(SUFFIX_CHAT_ROOM_STATUS_DESTINATION);
    }

    private Long extractUserId(Principal principal) {
        Authentication authentication = (Authentication) principal;

        return ((CustomUserDetails) authentication.getPrincipal()).getId();
    }

    private Long extractChatRoomId(String destination) {
        int prefixIndex = destination.indexOf(PREFIX_CHAT_ROOM_STATUS_DESTINATION) +
                PREFIX_CHAT_ROOM_STATUS_DESTINATION.length();
        int suffixIndex = destination.indexOf(SUFFIX_CHAT_ROOM_STATUS_DESTINATION);

        String strChatRoomId = destination.substring(prefixIndex, suffixIndex);

        return Long.valueOf(strChatRoomId);
    }

    private LocalDateTime convertTimestampToLocalDateTime(Long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private boolean hasPrincipal(Principal principal) {
        return principal != null;
    }

}
