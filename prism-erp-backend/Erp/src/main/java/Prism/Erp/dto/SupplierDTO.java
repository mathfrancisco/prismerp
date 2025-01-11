@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private Long id;
    private String name;
    private String documentNumber;
    private String contactName;
    private String email;
    private String phone;
    private SupplierStatus status;
    private String address;
    private String website;
    private String bankDetails;
    private Integer paymentTerms;
    private String taxRegime;
    private BigDecimal creditLimit;
    private BigDecimal qualityRating;
    private BigDecimal deliveryPerformance;
    private LocalDate lastEvaluationDate;
    private Set<String> categories;
    private Set<String> certifications;
    private List<SupplierDocumentDTO> documents;
    private List<SupplierContactDTO> contactHistory;
}