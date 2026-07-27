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

    // Get all notifications of logged-in user
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getAllNotifications(userId));
    }

    // Mark one notification as read
    @PatchMapping("/read/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @PathVariable Long notificationId) {

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId));
    }

    // Mark all notifications as read
    @PatchMapping("/read-all/{userId}")
    public ResponseEntity<String> markAllAsRead(
            @PathVariable Long userId) {

        notificationService.markAllAsRead(userId);

        return ResponseEntity.ok("All notifications marked as read.");
    }

    // Clear all notifications
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearAllNotifications(
            @PathVariable Long userId) {

        notificationService.clearAllNotifications(userId);

        return ResponseEntity.ok("All notifications cleared.");
    }
}