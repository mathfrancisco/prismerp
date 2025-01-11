@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDocumentDTO {
    private Long id;
    private String documentType;
    private String documentNumber;
    private LocalDate expirationDate;
    private String status;
    private String fileUrl;
    private LocalDateTime uploadDate;
}