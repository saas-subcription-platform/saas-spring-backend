package com.saas.springbackend.notification.service;

import com.saas.springbackend.notification.dto.NotificationResponseDTO;
import com.saas.springbackend.notification.entity.Notification;
import com.saas.springbackend.notification.entity.NotificationStatus;
import com.saas.springbackend.notification.entity.NotificationType;
import com.saas.springbackend.notification.repository.NotificationRepository;
import com.saas.springbackend.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final ModelMapper mapper;

    @Override
    public NotificationResponseDTO createNotification(
            User user,
            String title,
            String message,
            NotificationType type) {

        Notification notification = new Notification();
        notification.setCompany(user.getCompany());
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setUser(user);

        Notification savedNotification =
                notificationRepository.save(notification);

        NotificationResponseDTO response =
                mapper.map(savedNotification, NotificationResponseDTO.class);

        response.setNotificationId(savedNotification.getId());
        response.setUserId(user.getId());

        return response;
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications(Long userId) {

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notification -> {

                    NotificationResponseDTO dto =
                            mapper.map(notification, NotificationResponseDTO.class);

                    dto.setNotificationId(notification.getId());
                    dto.setUserId(userId);

                    return dto;

                })
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDTO markAsRead(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found"));

        notification.setStatus(NotificationStatus.READ);

        Notification updatedNotification =
                notificationRepository.save(notification);

        NotificationResponseDTO response =
                mapper.map(updatedNotification, NotificationResponseDTO.class);

        response.setNotificationId(updatedNotification.getId());
        response.setUserId(updatedNotification.getUser().getId());

        return response;
    }

    @Override
    public void markAllAsRead(Long userId) {

        List<Notification> notifications =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        notifications.forEach(notification ->
                notification.setStatus(NotificationStatus.READ));

        notificationRepository.saveAll(notifications);
    }

    @Override
    public void clearAllNotifications(Long userId) {

        List<Notification> notifications =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        notificationRepository.deleteAll(notifications);
    }
}