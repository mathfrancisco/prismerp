package Prism.Erp.service;

import Prism.Erp.dto.*;

public interface FinancialIntegrationService {
    AccountReceivableDTO createReceivableFromInvoice(Long invoiceId);
    PaymentPlanDTO createPaymentPlan(Long invoiceId, PaymentPlanDTO planDTO);
    FinancialTransactionDTO registerPayment(Long receivableId, FinancialTransactionDTO paymentDTO);
    FinancialSummaryDTO getCustomerFinancialSummary(Long customerId);
}