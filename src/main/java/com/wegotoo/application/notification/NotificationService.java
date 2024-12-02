package com.wegotoo.application.notification;

import com.wegotoo.application.notification.request.SandRequest;
import com.wegotoo.domain.notification.redis.Notification;
import com.wegotoo.domain.notification.redis.Subscribe;
import com.wegotoo.domain.notification.redis.Type;
import com.wegotoo.domain.notification.redis.repository.NotificationRepository;
import com.wegotoo.domain.notification.redis.repository.SubscribeRepository;
import com.wegotoo.domain.notification.repository.SseEmitterRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SubscribeRepository subscribeRepository;
    private final SseEmitterRepository sseEmitterRepository;
    private final NotificationRepository notificationRepository;
    private static final Long DEFAULT_TIMEOUT = 604800000L;

    @Transactional
    public SseEmitter subscribe(Long userId) throws IOException {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        Subscribe subscribe = Subscribe.create(userId);
        subscribeRepository.save(subscribe);

        sseEmitterRepository.save(userId, sseEmitter);
        sseEmitter.send(SseEmitter.event().name("Subscription Success").data(DEFAULT_TIMEOUT));

        lastMessageSend(userId, sseEmitter);

        sseEmitter.onCompletion(() -> handleDisconnection(userId));
        sseEmitter.onError(ex ->  handleDisconnection(userId));
        sseEmitter.onTimeout(() ->  handleDisconnection(userId));

        return sseEmitter;
    }

    @Transactional
    public void notifyChatting(SandRequest request) throws IOException {
        SseEmitter sseEmitter = sseEmitterRepository.findByUserId(request.getRecipientId());

        if (sseEmitter == null) {
            LocalDateTime now = LocalDateTime.now();
            notificationRepository.save(Notification.create(request, now, Type.CHAT));
            return;
        }

        sseEmitter.send(SseEmitter.event().name("ChatNotification").data(request));
    }

    public void lastMessageSend(Long userId, SseEmitter sseEmitter) {
        Optional<Notification> lastMessage = notificationRepository.findById(userId);
        lastMessage.ifPresent(notification -> {
            SandRequest request = SandRequest.of(notification.getUserId(), notification.getChatRoomId(),
                    notification.getMessage());

            try {
                sseEmitter.send(SseEmitter.event()
                        .name("LastChatMessage")
                        .data(request));
                notificationRepository.deleteById(userId);
            } catch (IOException e) {
                handleDisconnection(userId);
                throw new BusinessException(ErrorCode.FAILED_SEND_NOTIFICATION);
            }
        });
    }

    public void handleDisconnection(Long userId) {
        sseEmitterRepository.deleteByUserId(userId);
        subscribeRepository.deleteById(userId);
    }

}