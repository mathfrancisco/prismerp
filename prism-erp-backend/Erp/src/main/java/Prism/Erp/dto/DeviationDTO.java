@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviationDTO {
    private String type;
    private String description;
    private BigDecimal expectedValue;
    private BigDecimal actualValue;
    private BigDecimal deviation;
    private String impact;
    private LocalDateTime detectedAt;
    private String status;
}