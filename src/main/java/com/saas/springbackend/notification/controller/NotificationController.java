package com.saas.springbackend.notification.controller;

import com.saas.springbackend.notification.dto.NotificationResponseDTO;
import com.saas.springbackend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get all notifications of logged-in user
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                notificationService.getAllNotifications(email));
    }

    // Mark one notification as read
    @PatchMapping("/read/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @PathVariable Long notificationId) {

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId));
    }

    // Mark all notifications as read
    @PatchMapping("/read-all")
    public ResponseEntity<String> markAllAsRead(
            Authentication authentication) {

        String email = authentication.getName();

        notificationService.markAllAsRead(email);

        return ResponseEntity.ok("All notifications marked as read.");
    }

    // Clear all notifications
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllNotifications(
            Authentication authentication) {

        String email = authentication.getName();

        notificationService.clearAllNotifications(email);

        return ResponseEntity.ok("All notifications cleared.");
    }
}