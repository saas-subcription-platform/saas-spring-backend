package com.saas.springbackend.notification.service;

import com.saas.springbackend.notification.dto.NotificationResponseDTO;
import com.saas.springbackend.notification.entity.NotificationType;
import com.saas.springbackend.user.entity.User;

import java.util.List;

public interface NotificationService {

    NotificationResponseDTO createNotification(
            User user,
            String title,
            String message,
            NotificationType type);

    List<NotificationResponseDTO> getAllNotifications();

    void markAllAsRead();

    void clearAllNotifications();

    NotificationResponseDTO getLatestNotification();

    NotificationResponseDTO markAsRead(Long notificationId);

    NotificationResponseDTO createNotification(
            User user,
            String title,
            String message,
            NotificationType type,
            Long referenceId);
}