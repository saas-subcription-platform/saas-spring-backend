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

import com.saas.springbackend.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Override
    public NotificationResponseDTO createNotification(
            User user,
            String title,
            String message,
            NotificationType type) {

        Notification notification = new Notification();

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
    public List<NotificationResponseDTO> getAllNotifications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(notification -> {

                    NotificationResponseDTO dto =
                            mapper.map(notification, NotificationResponseDTO.class);

                    dto.setNotificationId(notification.getId());
                    dto.setUserId(user.getId());

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
    public void markAllAsRead(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> notifications =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        notifications.forEach(notification ->
                notification.setStatus(NotificationStatus.READ));

        notificationRepository.saveAll(notifications);
    }
    @Override
    public void clearAllNotifications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> notifications =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        notificationRepository.deleteAll(notifications);
    }
}