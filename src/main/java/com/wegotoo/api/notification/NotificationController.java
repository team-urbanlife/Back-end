package com.wegotoo.api.notification;

import com.wegotoo.api.ApiResponse;
import com.wegotoo.application.notification.NotificationService;
import com.wegotoo.infra.resolver.auth.Auth;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/v1/notification", produces = "text/event-stream")
    public SseEmitter subscribeToNotification(@Auth Long userId) throws IOException {
        return notificationService.subscribe(userId);
    }

}
