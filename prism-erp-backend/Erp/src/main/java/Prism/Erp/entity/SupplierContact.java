@Entity
@Table(name = "supplier_contacts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private String contactType;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime contactDate;

    private String contactedBy;

    private String outcome;
}