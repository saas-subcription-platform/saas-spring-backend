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


import com.saas.springbackend.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
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
    public List<NotificationResponseDTO> getAllNotifications() {

        User admin = getLoggedInUser();

        Long companyId = admin.getCompany().getId();

        return notificationRepository
                .findByCompanyIdOrderByCreatedAtDesc(companyId)
                .stream()
                .map(notification -> {

                    NotificationResponseDTO dto =
                            mapper.map(notification, NotificationResponseDTO.class);

                    dto.setNotificationId(notification.getId());
                    dto.setUserId(notification.getUser().getId());

                    return dto;
                })
                .toList();
    }

    @Transactional
    @Override
    public void markAllAsRead() {
        User admin = getLoggedInUser();
        Long companyId = admin.getCompany().getId();

        // Direct DB update instead of looping + saveAll
        notificationRepository.markAllAsReadByCompanyId(companyId);
    }




    @Override
    public void clearAllNotifications() {

        User admin = getLoggedInUser();

        Long companyId = admin.getCompany().getId();

        List<Notification> notifications =
                notificationRepository.findByCompanyIdOrderByCreatedAtDesc(companyId);

        notificationRepository.deleteAll(notifications);
    }
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public NotificationResponseDTO markAsRead(Long notificationId) {


        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException("Notification not found"));


        notification.setStatus(NotificationStatus.READ);


        Notification updatedNotification =
                notificationRepository.save(notification);


        NotificationResponseDTO dto =
                mapper.map(updatedNotification,
                        NotificationResponseDTO.class);


        dto.setNotificationId(updatedNotification.getId());

        dto.setUserId(updatedNotification.getUser().getId());


        return dto;
    }

}