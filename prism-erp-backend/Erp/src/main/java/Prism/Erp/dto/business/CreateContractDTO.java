@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractDTO {
    @NotBlank
    private String title;
    
    private String description;
    
    @NotNull
    private Long supplierId;
    
    @NotNull
    private ContractType type;
    
    @NotNull
    private LocalDate startDate;
    
    @NotNull
    private LocalDate endDate;
    
    @NotBlank
    private String currency;
    
    @NotEmpty
    private List<CreateContractItemDTO> items;
    
    private List<CreateMilestoneDTO> milestones;
    
    private List<MultipartFile> attachments;
}
