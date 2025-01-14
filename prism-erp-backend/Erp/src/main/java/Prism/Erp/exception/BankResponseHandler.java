package Prism.Erp.exception;

import Prism.Erp.dto.financial.PaymentResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BankResponseHandler {

    public PaymentResponse handleResponse(BankApiResponse bankResponse) {
        return PaymentResponse.builder()
                .success(bankResponse.isSuccessful())
                .transactionId(bankResponse.getTransactionId())
                .statusMessage(bankResponse.getMessage())
                .processedAt(LocalDateTime.now())
                .build();
    }
}
