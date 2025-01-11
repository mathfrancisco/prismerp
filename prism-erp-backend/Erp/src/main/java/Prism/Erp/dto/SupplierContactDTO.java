@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierContactDTO {
    private Long id;
    private String contactType;
    private String description;
    private LocalDateTime contactDate;
    private String contactedBy;
    private String outcome;
}