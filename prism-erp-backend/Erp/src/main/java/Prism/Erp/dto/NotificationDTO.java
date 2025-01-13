package Prism.Erp.dto;

import Prism.Erp.model.NotificationChannel;
import Prism.Erp.model.NotificationPriority;
import Prism.Erp.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationPriority priority;
    private Set<NotificationChannel> channels;
    private String recipient;
    private Map<String, Object> metadata;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private LocalDateTime expiresAt;
}