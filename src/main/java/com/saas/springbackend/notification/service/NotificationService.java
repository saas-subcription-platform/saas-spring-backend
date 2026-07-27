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

    List<NotificationResponseDTO> getAllNotifications(String email);

    NotificationResponseDTO markAsRead(Long notificationId);

    void markAllAsRead(String email);

    void clearAllNotifications(String email);
}