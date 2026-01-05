package com.cc.project.Controllers;

import com.cc.project.Entity.Notification;
import com.cc.project.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get all notifications for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notifications for a user
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notification count
     */
    @GetMapping("/user/{userId}/unread/count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        Long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Mark a notification as read
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    /**
     * Mark all notifications as read for a user
     */
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a notification
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}
