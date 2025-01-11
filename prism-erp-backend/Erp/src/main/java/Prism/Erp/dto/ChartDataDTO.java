@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataDTO {
    private String label;
    private Object value;
    private String category;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
}
