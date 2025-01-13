package Prism.Erp.service.business.audit;

import Prism.Erp.entity.business.Audit.AuditLog;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createAuditLog(AuditLogDTO auditLogDTO) {
        try {
            AuditLog auditLog = modelMapper.map(auditLogDTO, AuditLog.class);
            auditLogRepository.save(auditLog);
            log.debug("Created audit log: {}", auditLog);
        } catch (Exception e) {
            log.error("Failed to create audit log: {}", e.getMessage());
            throw new AuditException("Failed to create audit log", e);
        }
    }

    @Override
    public Page<AuditLogDTO> searchAuditLogs(AuditSearchCriteria criteria, Pageable pageable) {
        Page<AuditLog> logs = auditLogRepository.findAll(
                buildSpecification(criteria),
                pageable
        );
        return logs.map(log -> modelMapper.map(log, AuditLogDTO.class));
    }

    @Override
    public List<AuditLogDTO> getEntityHistory(String entityType, String entityId) {
        List<AuditLog> logs = auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(
                entityType,
                entityId
        );
        return logs.stream()
                .map(log -> modelMapper.map(log, AuditLogDTO.class))
                .collect(Collectors.toList());
    }
}