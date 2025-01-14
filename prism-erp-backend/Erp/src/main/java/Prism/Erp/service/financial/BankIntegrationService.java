package Prism.Erp.service.financial;

import Prism.Erp.dto.financial.BankReconciliationDTO;
import Prism.Erp.dto.financial.PaymentRequest;
import Prism.Erp.dto.financial.PaymentResponse;
import Prism.Erp.exception.BankResponseHandler;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class BankIntegrationService {

    @Value("${bank.api.url}")
    private String bankApiUrl;

    @Value("${bank.api.key}")
    private String bankApiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BankResponseHandler responseHandler;

    public PaymentResponse submitPayment(PaymentRequest request) {
        log.info("Submitting payment to bank: {}", request.getDocumentNumber());

        try {
            // Preparar cabeçalhos
            HttpHeaders headers = createHeaders();

            // Criar requisição HTTP
            HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);

            // Enviar requisição para o banco
            ResponseEntity<BankApiResponse> response = restTemplate.exchange(
                    bankApiUrl + "/payments",
                    HttpMethod.POST,
                    entity,
                    BankApiResponse.class
            );

            // Processar resposta
            return responseHandler.handleResponse(response.getBody());

        } catch (RestClientException e) {
            log.error("Error communicating with bank API: {}", e.getMessage());
            throw new BankIntegrationException("Failed to communicate with bank", e);
        }
    }

    public BankReconciliationDTO reconcileTransactions(Long bankAccountId, LocalDate date) {
        log.info("Reconciling transactions for account {} on date {}", bankAccountId, date);

        try {
            // Buscar extrato bancário
            BankStatement bankStatement = fetchBankStatement(bankAccountId, date);

            // Buscar transações do sistema
            List<SystemTransaction> systemTransactions = fetchSystemTransactions(bankAccountId, date);

            // Realizar reconciliação
            return performReconciliation(bankStatement, systemTransactions);

        } catch (Exception e) {
            log.error("Error during reconciliation: {}", e.getMessage());
            throw new ReconciliationException("Failed to reconcile transactions", e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-Key", bankApiKey);
        headers.set("X-Transaction-ID", generateTransactionId());
        return headers;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}
