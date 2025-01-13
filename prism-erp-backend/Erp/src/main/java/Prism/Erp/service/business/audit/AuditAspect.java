package Prism.Erp.service.business.audit;

import Prism.Erp.model.business.AuditAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Auditable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Around("@annotation(auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Object oldValue = null;
        String entityId = extractEntityId(joinPoint);

        // Captura o valor antigo se for UPDATE
        if (auditable.action() == AuditAction.UPDATE) {
            oldValue = getCurrentEntityValue(auditable.entity(), entityId);
        }

        // Executa o método
        Object result = joinPoint.proceed();

        // Cria o log de auditoria
        createAuditLog(auditable, entityId, oldValue, result);

        return result;
    }

    private void createAuditLog(Auditable auditable, String entityId, Object oldValue, Object newValue) {
        AuditLogDTO auditLog = AuditLogDTO.builder()
                .entityType(auditable.entity())
                .entityId(entityId)
                .action(auditable.action())
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .oldValue(convertToJson(oldValue))
                .newValue(convertToJson(newValue))
                .description(auditable.description())
                .ipAddress(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();

        auditService.createAuditLog(auditLog);
    }
}

