package com.krushimitra.app.dto.response;

import com.krushimitra.app.entity.Notification;
import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private Notification.NotificationType type;
    private boolean read;
    private boolean global;
    private Long relatedSchemeId;
    private Long relatedApplicationId;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Notification.NotificationType getType() { return type; }
    public void setType(Notification.NotificationType type) { this.type = type; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public boolean isGlobal() { return global; }
    public void setGlobal(boolean global) { this.global = global; }
    public Long getRelatedSchemeId() { return relatedSchemeId; }
    public void setRelatedSchemeId(Long relatedSchemeId) { this.relatedSchemeId = relatedSchemeId; }
    public Long getRelatedApplicationId() { return relatedApplicationId; }
    public void setRelatedApplicationId(Long relatedApplicationId) { this.relatedApplicationId = relatedApplicationId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
