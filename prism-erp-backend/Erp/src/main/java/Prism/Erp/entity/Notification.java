package Prism.Erp.entity;

import Prism.Erp.model.NotificationChannel;
import Prism.Erp.model.NotificationPriority;
import Prism.Erp.model.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationPriority priority;
    
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<NotificationChannel> channels;
    
    @Column(nullable = false)
    private String recipient;
    
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> metadata;
    
    private boolean read;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime readAt;
    
    private LocalDateTime expiresAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}