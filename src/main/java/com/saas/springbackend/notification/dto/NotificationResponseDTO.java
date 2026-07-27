package com.saas.springbackend.notification.dto;

import com.saas.springbackend.notification.entity.NotificationStatus;
import com.saas.springbackend.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long notificationId;

    private String title;

    private String message;

    private NotificationType type;

    private NotificationStatus status;

    private Long userId;

    private LocalDateTime createdAt;
}