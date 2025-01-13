package Prism.Erp.service.business.edi;

import Prism.Erp.entity.business.edi.EdiConfiguration;
import Prism.Erp.entity.business.edi.EdiTransaction;
import Prism.Erp.model.business.EdiProtocol;
import Prism.Erp.model.business.EdiStatus;
import Prism.Erp.model.business.EdiTransactionType;
import Prism.Erp.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EdiService {

    private final EdiConfigurationRepository configurationRepository;
    private final EdiTransactionRepository transactionRepository;
    private final EdiTransformerFactory transformerFactory;
    private final EdiValidatorFactory validatorFactory;
    private final EdiProtocolFactory protocolFactory;
    private final NotificationService notificationService;

    public void processInboundMessage(String supplierId, String rawMessage, EdiTransactionType type) {
        EdiConfiguration config = configurationRepository.findBySupplierIdAndActive(supplierId, true)
                .orElseThrow(() -> new EdiConfigurationNotFoundException(supplierId));

        EdiTransaction transaction = createTransaction(config, rawMessage, type);

        try {
            // Validar mensagem
            EdiValidator validator = validatorFactory.getValidator(config.getFormat());
            validator.validate(rawMessage);

            // Transformar mensagem
            EdiTransformer transformer = transformerFactory.getTransformer(config.getFormat());
            Object processedData = transformer.transform(rawMessage, config.getMappings());

            // Atualizar transação
            transaction.setProcessedData(new ObjectMapper().writeValueAsString(processedData));
            transaction.setStatus(EdiStatus.COMPLETED);
            transaction.setProcessedAt(LocalDateTime.now());

            // Processar dados transformados
            processTransformedData(processedData, type);

            // Enviar acknowledgment
            sendAcknowledgment(transaction);

        } catch (Exception e) {
            log.error("Error processing EDI message: {}", e.getMessage());
            transaction.setStatus(EdiStatus.ERROR);
            transaction.setErrorMessage(e.getMessage());
            notificationService.createEdiAlert(config.getSupplier(), e.getMessage());
        }

        transactionRepository.save(transaction);
    }

    public void sendOutboundMessage(String supplierId, Object data, EdiTransactionType type) {
        EdiConfiguration config = configurationRepository.findBySupplierIdAndActive(supplierId, true)
                .orElseThrow(() -> new EdiConfigurationNotFoundException(supplierId));

        try {
            // Transformar dados para formato EDI
            EdiTransformer transformer = transformerFactory.getTransformer(config.getFormat());
            String ediMessage = transformer.transformToEdi(data, config.getMappings());

            // Criar transação
            EdiTransaction transaction = createTransaction(config, ediMessage, type);

            // Enviar mensagem
            EdiProtocol protocol = protocolFactory.getProtocol(config.getProtocol());
            protocol.send(config.getEndpoint(), ediMessage, config.getCredentials());

            // Atualizar transação
            transaction.setStatus(EdiStatus.COMPLETED);
            transaction.setProcessedAt(LocalDateTime.now());

            transactionRepository.save(transaction);

        } catch (Exception e) {
            log.error("Error sending EDI message: {}", e.getMessage());
            notificationService.createEdiAlert(config.getSupplier(), e.getMessage());
            throw new EdiSendException("Failed to send EDI message", e);
        }
    }
}

