@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelayDTO {
    private String processType;
    private String reference;
    private LocalDateTime expectedDate;
    private LocalDateTime estimatedDate;
    private Long delayInDays;
    private String reason;
    private String status;
    private String responsibleParty;
}