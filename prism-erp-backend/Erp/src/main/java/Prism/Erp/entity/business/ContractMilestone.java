@Entity
@Table(name = "contract_milestones")
@Getter @Setter
public class ContractMilestone extends TenantAwareEntity {
    
    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private LocalDate dueDate;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MilestoneStatus status;
    
    private LocalDate completedDate;
    
    private String completedBy;
    
    @Column(nullable = false)
    private BigDecimal value;
    
    private String notes;
}
