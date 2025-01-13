@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
    private Long id;
    private String contractNumber;
    private String title;
    private String description;
    private SupplierDTO supplier;
    private ContractType type;
    private ContractStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalValue;
    private String currency;
    private List<ContractItemDTO> items;
    private List<ContractMilestoneDTO> milestones;
    private List<ContractAttachmentDTO> attachments;
}
