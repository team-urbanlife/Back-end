package com.wegotoo.application.notification;

import com.wegotoo.application.notification.request.SendRequest;
import com.wegotoo.domain.notification.redis.Notification;
import com.wegotoo.domain.notification.redis.Subscribe;
import com.wegotoo.domain.notification.redis.Type;
import com.wegotoo.domain.notification.redis.repository.SubscribeRepository;
import com.wegotoo.domain.notification.repository.SseEmitterRepository;
import com.wegotoo.exception.BusinessException;
import com.wegotoo.exception.ErrorCode;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 604800000L;
    public static final int START = 0;
    public static final int END = -1;
    public static final int DAYS = 7;
    public static final int PARALLEL_PROCESSING_THRESHOLD = 10;

    private final SubscribeRepository subscribeRepository;
    private final SseEmitterRepository sseEmitterRepository;
    private final RedisTemplate<String, Notification> redisTemplate;

    public SseEmitter subscribe(Long userId) throws IOException {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        Subscribe subscribe = Subscribe.create(userId);
        subscribeRepository.save(subscribe);

        sseEmitterRepository.save(userId, sseEmitter);
        sseEmitter.send(SseEmitter.event().name("Subscription Success").data(DEFAULT_TIMEOUT));

        sendStoredNotifications(userId, sseEmitter);

        registerDisconnectionHandlers(userId, sseEmitter);

        return sseEmitter;
    }

    public void notifyChatting(SendRequest request) throws IOException {
        SseEmitter sseEmitter = sseEmitterRepository.findByUserId(request.getRecipientId());

        if (sseEmitter == null) {
            saveOfflineUserMessage(request);
            return;
        }

        sendMessage(sseEmitter, request);
    }

    private void sendStoredNotifications(Long userId, SseEmitter sseEmitter) {
        String key = String.valueOf(userId);

        List<Notification> notifications = redisTemplate.opsForList().range(key, START, END);

        sendLastMessages(sseEmitter, notifications);
    }

    private void sendLastMessages(SseEmitter sseEmitter, List<Notification> notifications) {
        if (isNotEmpty(notifications)) {
            if (shouldUseParallelProcessing(notifications)) {
                handleStreamedMessages(notifications.parallelStream(), sseEmitter);
                return;
            }
            handleStreamedMessages(notifications.stream(), sseEmitter);
        }
    }

    private boolean shouldUseParallelProcessing(List<Notification> notifications) {
        return notifications.size() >= PARALLEL_PROCESSING_THRESHOLD;
    }

    private void handleStreamedMessages(Stream<Notification> notifications, SseEmitter sseEmitter) {
        notifications
                .map(this::convertToSendRequest)
                .forEach(request -> sendMessage(sseEmitter, request));
    }

    private SendRequest convertToSendRequest(Notification notification) {
        return SendRequest.of(
                notification.getUserId(),
                notification.getChatRoomId(),
                notification.getMessage()
        );
    }

    private boolean isNotEmpty(List<Notification> notifications) {
        return notifications != null && !notifications.isEmpty();
    }

    private void sendMessage(SseEmitter sseEmitter, SendRequest request) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("ChatNotification")
                    .data(request));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FAILED_SEND_NOTIFICATION);
        }
    }

    private void registerDisconnectionHandlers(Long userId, SseEmitter sseEmitter) {
        sseEmitter.onCompletion(() -> handleDisconnection(userId));
        sseEmitter.onError(ex -> handleDisconnection(userId));
        sseEmitter.onTimeout(() -> handleDisconnection(userId));
    }

    private void handleDisconnection(Long userId) {
        sseEmitterRepository.deleteByUserId(userId);
        subscribeRepository.deleteById(userId);
    }

    private void saveOfflineUserMessage(SendRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String key = String.valueOf(request.getRecipientId());

        Notification notification = Notification.create(request, now, Type.CHAT);
        redisTemplate.opsForList().rightPush(key, notification);

        redisTemplate.expire(key, Duration.ofDays(DAYS));
    }

}