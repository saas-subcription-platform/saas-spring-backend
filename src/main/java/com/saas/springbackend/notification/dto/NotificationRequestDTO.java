package com.saas.springbackend.notification.dto;

import com.saas.springbackend.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {

    private String title;

    private String message;

    private NotificationType type;

    private Long userId;
}