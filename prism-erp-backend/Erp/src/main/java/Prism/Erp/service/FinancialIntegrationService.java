package Prism.Erp.service;

import Prism.Erp.dto.*;

public interface FinancialIntegrationService {
    AccountReceivableDTO createReceivableFromInvoice(Long invoiceId);
    PaymentPlanDTO createPaymentPlan(Long invoiceId, PaymentPlanDTO planDTO);
    FinancialTransactionDTO registerPayment(Long receivableId, FinancialTransactionDTO paymentDTO);
    FinancialSummaryDTO getCustomerFinancialSummary(Long customerId);

void createPayableFromPurchaseOrder(PurchaseOrderDTO purchaseOrder);
    void updatePayableStatus(Long purchaseOrderId, PaymentStatus status);
    boolean checkBudgetAvailability(String costCenter, BigDecimal amount);
    void createSupplierFinancialRecord(SupplierDTO supplier);
    Map<String, Object> getFinancialMetrics(Long supplierId);
}