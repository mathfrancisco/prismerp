package Prism.Erp.repository;

import Prism.Erp.entity.Notification;
import Prism.Erp.model.NotificationPriority;
import Prism.Erp.model.NotificationType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientAndReadOrderByCreatedAtDesc(String recipient, boolean read);
    List<Notification> findByTypeAndExpiresAtGreaterThan(NotificationType type, LocalDateTime date);
    List<Notification> findByPriorityAndReadAndExpiresAtGreaterThan(
            NotificationPriority priority, boolean read, LocalDateTime date);
    
    @Modifying
    @Query("UPDATE Notification n SET n.read = true, n.readAt = CURRENT_TIMESTAMP " +
           "WHERE n.id = :id AND n.recipient = :recipient")
    int markAsRead(@Param("id") Long id, @Param("recipient") String recipient);
}
