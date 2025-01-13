package Prism.Erp.dto.business.purchase;

import Prism.Erp.dto.business.dashboard.DeviationDTO;
import Prism.Erp.dto.KpiDTO;
import Prism.Erp.model.business.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseAnalyticsDTO {
    private BigDecimal totalPurchaseAmount;
    private Integer totalOrders;
    private Map<PurchaseOrderStatus, Integer> ordersByStatus;
    private Map<String, BigDecimal> purchasesBySupplier;
    private Map<String, BigDecimal> purchasesByCategory;
    private Double averageOrderValue;
    private List<DeviationDTO> significantDeviations;
    private Map<String, KpiDTO> kpis;
}