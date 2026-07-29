package com.saas.springbackend.notification.controller;

import com.saas.springbackend.notification.dto.NotificationResponseDTO;
import com.saas.springbackend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get all notifications of logged-in admin's company
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {

        return ResponseEntity.ok(
                notificationService.getAllNotifications());
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
    public ResponseEntity<String> markAllAsRead() {

        notificationService.markAllAsRead();

        return ResponseEntity.ok("All notifications marked as read.");
    }

    // Clear all notifications
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllNotifications() {

        notificationService.clearAllNotifications();

        return ResponseEntity.ok("All notifications cleared.");
    }

    @GetMapping("/latest")
    public ResponseEntity<NotificationResponseDTO> getLatestNotification() {

        return ResponseEntity.ok(
                notificationService.getLatestNotification()
        );
    }
}