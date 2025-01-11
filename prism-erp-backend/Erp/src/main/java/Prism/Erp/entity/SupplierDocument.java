@Entity
@Table(name = "supplier_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String documentNumber;

    private LocalDate expirationDate;

    private String status;

    private String fileUrl;

    private LocalDateTime uploadDate;
}