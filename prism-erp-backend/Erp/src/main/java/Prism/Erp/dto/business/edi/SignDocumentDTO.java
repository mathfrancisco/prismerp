@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignDocumentDTO {
    @NotBlank
    private String certificate;
    
    @NotBlank
    private String signature;
    
    private Map<String, String> metadata;
}
