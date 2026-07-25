package com.krushimitra.app.controller;

import com.krushimitra.app.dto.response.ApiResponse;
import com.krushimitra.app.dto.response.NotificationResponse;
import com.krushimitra.app.entity.Notification;
import com.krushimitra.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for notifications
 */
@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    @Autowired private NotificationService notificationService;

    @GetMapping("/my")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getMyNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched",
                notificationService.getMyNotifications(
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
        return ResponseEntity.ok(ApiResponse.success("Unread count", notificationService.getUnreadCount()));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", null));
    }

    @PostMapping("/broadcast")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> broadcast(
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(defaultValue = "GENERAL") Notification.NotificationType type) {
        notificationService.sendGlobalNotification(title, message, type);
        return ResponseEntity.ok(ApiResponse.success("Notification broadcast sent", null));
    }
}
