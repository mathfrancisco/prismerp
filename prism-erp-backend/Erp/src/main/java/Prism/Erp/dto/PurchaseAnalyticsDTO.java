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