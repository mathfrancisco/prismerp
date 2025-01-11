@Entity
@Table(name = "purchase_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    private LocalDateTime expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseOrderStatus status;

    @Enumerated(EnumType.STRING)
    private FreightType freightType;

    @Column(precision = 19, scale = 4)
    private BigDecimal subtotal;

    @Column(precision = 19, scale = 4)
    private BigDecimal taxAmount;

    @Column(precision = 19, scale = 4)
    private BigDecimal freightCost;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(length = 1000)
    private String notes;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderPayment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderApproval> approvals = new ArrayList<>();

    @OneToOne(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private PurchaseOrderHistory history;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderAttachment> attachments = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;
}