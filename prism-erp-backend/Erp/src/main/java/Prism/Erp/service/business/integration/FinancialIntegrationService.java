package Prism.Erp.service.business.integration;

import Prism.Erp.dto.*;
import Prism.Erp.dto.business.purchase.PurchaseOrderDTO;
import Prism.Erp.dto.business.supplier.SupplierDTO;
import Prism.Erp.model.business.PaymentStatus;

import java.math.BigDecimal;
import java.util.Map;

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