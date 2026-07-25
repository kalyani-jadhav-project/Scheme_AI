package com.krushimitra.app.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType type;

    @Column(name = "is_read")
    private boolean read = false;

    @Column(name = "is_global")
    private boolean global = false;

    @Column(name = "related_scheme_id")
    private Long relatedSchemeId;

    @Column(name = "related_application_id")
    private Long relatedApplicationId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Notification() {}

    public enum NotificationType {
        APPLICATION_STATUS, NEW_SCHEME, DOCUMENT_REQUIRED, DEADLINE_REMINDER,
        DISBURSEMENT, GENERAL, ALERT
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Farmer getFarmer() { return farmer; }
    public void setFarmer(Farmer farmer) { this.farmer = farmer; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
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
