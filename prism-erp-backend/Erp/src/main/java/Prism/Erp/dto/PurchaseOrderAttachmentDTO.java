@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderAttachmentDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private LocalDateTime uploadDate;
    private String uploadedBy;
    private String description;
}