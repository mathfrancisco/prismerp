package Prism.Erp.service;

import Prism.Erp.dto.NotificationDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    void sendNotification(NotificationDTO notificationDTO);
    List<NotificationDTO> getUnreadNotifications(String recipient);
    void markAsRead(Long notificationId, String recipient);
    void deleteExpiredNotifications();
    void createApprovalNotification(Long purchaseOrderId, String approver);
    void createCreditLimitWarning(Long supplierId, BigDecimal currentUsage, BigDecimal limit);
    void createOrderDelayNotification(Long purchaseOrderId, Long delayInDays);
    void createDocumentExpirationWarning(Long supplierId, String documentType, LocalDate expirationDate);
}