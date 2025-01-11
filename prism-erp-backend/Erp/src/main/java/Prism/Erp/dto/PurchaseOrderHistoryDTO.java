@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderHistoryDTO {
    private Long id;
    private Long purchaseOrderId;
    private List<PurchaseOrderEventDTO> events;
}