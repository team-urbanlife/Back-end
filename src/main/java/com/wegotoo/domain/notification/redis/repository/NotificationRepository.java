package com.wegotoo.domain.notification.redis.repository;

import com.wegotoo.domain.notification.redis.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

}
