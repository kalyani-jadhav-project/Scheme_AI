package com.krushimitra.app.service;

import com.krushimitra.app.dto.response.NotificationResponse;
import com.krushimitra.app.entity.Notification;
import com.krushimitra.app.exception.ResourceNotFoundException;
import com.krushimitra.app.repository.FarmerRepository;
import com.krushimitra.app.repository.NotificationRepository;
import com.krushimitra.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for notification management
 */
@Service
@Transactional
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private FarmerRepository farmerRepository;

    public Page<NotificationResponse> getMyNotifications(Pageable pageable) {
        UserPrincipal currentUser = getCurrentUser();
        var farmer = farmerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));
        return notificationRepository
                .findByFarmerIdOrGlobalTrue(farmer.getId(), pageable)
                .map(this::mapToNotificationResponse);
    }

    public List<NotificationResponse> getRecentNotifications(Long farmerId) {
        return notificationRepository
                .findTop5ByFarmerIdOrGlobalTrueOrderByCreatedAtDesc(farmerId)
                .stream()
                .map(this::mapToNotificationResponse)
                .collect(Collectors.toList());
    }

    public long getUnreadCount() {
        UserPrincipal currentUser = getCurrentUser();
        var farmer = farmerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));
        return notificationRepository.countByFarmerIdAndReadFalse(farmer.getId());
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void sendGlobalNotification(String title, String message, Notification.NotificationType type) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setGlobal(true);
        notificationRepository.save(notification);
    }

    private NotificationResponse mapToNotificationResponse(Notification n) {
        NotificationResponse response = new NotificationResponse();
        response.setId(n.getId());
        response.setTitle(n.getTitle());
        response.setMessage(n.getMessage());
        response.setType(n.getType());
        response.setRead(n.isRead());
        response.setGlobal(n.isGlobal());
        response.setRelatedSchemeId(n.getRelatedSchemeId());
        response.setRelatedApplicationId(n.getRelatedApplicationId());
        response.setCreatedAt(n.getCreatedAt());
        return response;
    }

    private UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
