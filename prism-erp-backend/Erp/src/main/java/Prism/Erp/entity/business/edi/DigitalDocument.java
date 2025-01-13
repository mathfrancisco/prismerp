@Entity
@Table(name = "digital_documents")
@Getter @Setter
public class DigitalDocument extends TenantAwareEntity {
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentType type;
    
    @Column(nullable = false)
    private String documentNumber;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
    
    @Column(nullable = false)
    private String contentHash;
    
    @Column(nullable = false)
    private String storageLocation;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime expiresAt;
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<DigitalSignature> signatures;
    
    @ElementCollection
    private List<String> requiredSigners;
}
