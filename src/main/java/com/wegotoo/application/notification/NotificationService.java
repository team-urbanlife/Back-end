package com.wegotoo.application.notification;

import com.wegotoo.application.chat.request.ChatSendServiceRequest;
import com.wegotoo.domain.notification.Notification;
import com.wegotoo.domain.notification.repository.NotificationRepository;
import com.wegotoo.domain.notification.repository.SseEmitterRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SseEmitterRepository sseEmitterRepository;
    private final NotificationRepository notificationRepository;

    private final String SUBSCRIBE_NOTIFICATION = "Subscription Success";
    private final String CHATTING_NOTIFICATION = "Chat Notification";

    @Transactional
    public SseEmitter subscribe(Long userId) throws IOException {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        List<Notification> notifications = notificationRepository.findByReceiverId(userId);
        if (hasNotifications(notifications)) {
            sendNotifications(notifications, sseEmitter);
            notificationRepository.deleteAllByReceiverId(userId);
        }

        setupSseEmitterHandlers(userId, sseEmitter);

        sseEmitter.send(SseEmitter.event().name(SUBSCRIBE_NOTIFICATION));

        sseEmitterRepository.addEmitter(userId, sseEmitter);

        return sseEmitter;
    }

    @Transactional
    public void notifyChatting(Long receiverId, ChatSendServiceRequest request) {
        if (isUserSubscribed(receiverId)) {
            SseEmitter sseEmitter = sseEmitterRepository.getEmitter(receiverId);
            sendNotification(sseEmitter, request);
        } else {
            Notification notification = Notification.create(receiverId, request.getMessage());
            notificationRepository.save(notification);
        }
    }

    private static boolean hasNotifications(List<Notification> notifications) {
        return !notifications.isEmpty();
    }

    private void sendNotifications(List<Notification> notifications, SseEmitter sseEmitter) {
        notifications.stream()
                .map(Notification::getMessage)
                .forEach(message -> sendNotification(sseEmitter, message));
    }

    private void sendNotification(SseEmitter sseEmitter, ChatSendServiceRequest request) {
        try {
            sseEmitter.send(SseEmitter.event().name(CHATTING_NOTIFICATION).data(request));
        } catch (IOException e) {
            log.error("알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    private void setupSseEmitterHandlers(Long userId, SseEmitter sseEmitter) {
        sseEmitter.onCompletion(() -> sseEmitterRepository.removeEmitter(userId));
        sseEmitter.onTimeout(() -> sseEmitterRepository.removeEmitter(userId));
        sseEmitter.onError(e -> sseEmitterRepository.removeEmitter(userId));
    }

    private boolean isUserSubscribed(Long receiverId) {
        return sseEmitterRepository.containsEmitter(receiverId);
    }

}
