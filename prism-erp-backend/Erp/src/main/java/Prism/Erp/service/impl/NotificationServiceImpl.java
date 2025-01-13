package Prism.Erp.service.impl;

import Prism.Erp.dto.NotificationDTO;
import Prism.Erp.entity.Notification;
import Prism.Erp.model.NotificationChannel;
import Prism.Erp.model.NotificationPriority;
import Prism.Erp.model.NotificationType;
import Prism.Erp.repository.NotificationRepository;
import Prism.Erp.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SmsService smsService;
    private final PushNotificationService pushService;
    private final WebhookService webhookService;
    
    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return convertToDTO(notification);
    }
    
    @Override
    @Async
    public void sendNotification(NotificationDTO notificationDTO) {
        NotificationDTO savedNotification = createNotification(notificationDTO);
        
        for (NotificationChannel channel : notificationDTO.getChannels()) {
            try {
                switch (channel) {
                    case EMAIL:
                        emailService.sendEmail(buildEmailNotification(savedNotification));
                        break;
                    case SMS:
                        smsService.sendSms(buildSmsNotification(savedNotification));
                        break;
                    case PUSH:
                        pushService.sendPushNotification(buildPushNotification(savedNotification));
                        break;
                    case WEBHOOK:
                        webhookService.sendWebhookNotification(buildWebhookPayload(savedNotification));
                        break;
                    default:
                        log.warn("Unsupported notification channel: {}", channel);
                }
            } catch (Exception e) {
                log.error("Failed to send notification through channel {}: {}", channel, e.getMessage());
            }
        }
    }
    
    @Override
    @Scheduled(cron = "0 0 1 * * ?") // Run daily at 1 AM
    public void deleteExpiredNotifications() {
        List<Notification> expiredNotifications = notificationRepository
            .findByTypeAndExpiresAtGreaterThan(null, LocalDateTime.now());
        notificationRepository.deleteAll(expiredNotifications);
    }
    
    @Override
    public void createApprovalNotification(Long purchaseOrderId, String approver) {
        NotificationDTO notification = NotificationDTO.builder()
            .title("Purchase Order Approval Required")
            .message(String.format("Purchase Order #%d requires your approval", purchaseOrderId))
            .type(NotificationType.APPROVAL_REQUIRED)
            .priority(NotificationPriority.HIGH)
            .channels(Set.of(NotificationChannel.EMAIL, NotificationChannel.SYSTEM))
            .recipient(approver)
            .metadata(Map.of("purchaseOrderId", purchaseOrderId))
            .expiresAt(LocalDateTime.now().plusDays(7))
            .build();
            
        sendNotification(notification);
    }
    
    // Additional implementation methods...
}