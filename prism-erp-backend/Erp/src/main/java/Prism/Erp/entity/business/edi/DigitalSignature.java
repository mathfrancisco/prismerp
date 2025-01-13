@Entity
@Table(name = "digital_signatures")
@Getter @Setter
public class DigitalSignature extends TenantAwareEntity {
    
    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private DigitalDocument document;
    
    @Column(nullable = false)
    private String signerName;
    
    @Column(nullable = false)
    private String signerEmail;
    
    @Column(nullable = false)
    private String signatureHash;
    
    @Column(nullable = false)
    private String certificateInfo;
    
    @Column(nullable = false)
    private LocalDateTime signedAt;
    
    @Column(nullable = false)
    private String ipAddress;
    
    @Convert(converter = JsonConverter.class)
    private Map<String, String> metadata;
}
