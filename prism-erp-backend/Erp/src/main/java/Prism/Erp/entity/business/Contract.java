@Entity
@Table(name = "contracts")
@Getter @Setter
public class Contract extends TenantAwareEntity {
    
    @Column(nullable = false, unique = true)
    private String contractNumber;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContractType type;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContractStatus status;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private BigDecimal totalValue;
    
    @Column(nullable = false)
    private String currency;
    
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<ContractItem> items;
    
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<ContractMilestone> milestones;
    
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<ContractAttachment> attachments;
    
    @Version
    private Long version;
    
    @Column(columnDefinition = "jsonb")
    private String metadata;
}
