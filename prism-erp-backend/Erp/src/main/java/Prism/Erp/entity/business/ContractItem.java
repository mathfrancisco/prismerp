@Entity
@Table(name = "contract_items")
@Getter @Setter
public class ContractItem extends TenantAwareEntity {
    
    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
    
    @Column(nullable = false)
    private String itemNumber;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal quantity;
    
    @Column(nullable = false)
    private String unit;
    
    @Column(nullable = false)
    private BigDecimal unitPrice;
    
    @Column(nullable = false)
    private BigDecimal totalPrice;
    
    private String category;
    
    @ElementCollection
    private Map<String, String> specifications;
}
