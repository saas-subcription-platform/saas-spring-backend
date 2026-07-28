package com.saas.springbackend.notification.repository;

import com.saas.springbackend.notification.entity.Notification;
import com.saas.springbackend.notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndStatus(
            Long userId,
            NotificationStatus status);
}