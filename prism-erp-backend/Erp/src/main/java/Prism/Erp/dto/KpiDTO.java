@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KpiDTO {
    private String name;
    private String description;
    private Object value;
    private Object target;
    private String unit;
    private String status;
    private BigDecimal trend;
    private LocalDateTime lastUpdate;
}