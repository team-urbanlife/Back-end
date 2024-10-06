package com.wegotoo.application.notification;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("A유저가 접속한 상태에서 메시지 알림을 전송 받을 수 있다.")
    public void testNotifyChatting() throws IOException {
        // given
        Long receiverId = 1L;

        notificationService.subscribe(receiverId);

        // when
        notificationService.notifyChatting(receiverId, null);

    }
}