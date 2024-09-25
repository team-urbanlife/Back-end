package com.wegotoo.domain.notification.repository;

import com.wegotoo.domain.notification.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverId(Long userId);

    void deleteAllByReceiverId(Long userId);
}
