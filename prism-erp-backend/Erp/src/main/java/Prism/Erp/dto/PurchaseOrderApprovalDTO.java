@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderApprovalDTO {
    private Long id;
    private Long purchaseOrderId;
    private String approverName;
    private String approverRole;
    private ApprovalStatus status;
    private LocalDateTime approvalDate;
    private String comments;
}