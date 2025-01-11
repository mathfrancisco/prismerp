@Entity
@Table(name = "purchase_order_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String event;

    @Column(length = 1000)
    private String description;

    private String username;

    @Convert(converter = JsonAttributeConverter.class)
    private Map<String, Object> changes;
}