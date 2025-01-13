@Entity
@Table(name = "contract_attachments")
@Getter @Setter
public class ContractAttachment extends TenantAwareEntity {
    
    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private String contentType;
    
    @Column(nullable = false)
    private String storageLocation;
    
    @Column(nullable = false)
    private Long fileSize;
    
    @Column(nullable = false)
    private LocalDateTime uploadedAt;
    
    @Column(nullable = false)
    private String uploadedBy;
}
