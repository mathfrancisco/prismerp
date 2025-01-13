@RestController
@RequestMapping("/api/v1/digital-signature")
@RequiredArgsConstructor
@Validated
public class DigitalSignatureController {

    private final DigitalSignatureService signatureService;

    @PostMapping("/documents")
    @PreAuthorize("hasRole('DOCUMENT_MANAGER')")
    public ResponseEntity<DigitalDocumentDTO> createDocument(
            @Valid @RequestBody CreateDocumentDTO createDTO) {
        return ResponseEntity.ok(signatureService.createDocument(createDTO));
    }
    
    @PostMapping("/documents/{documentNumber}/signatures")
    public ResponseEntity<SignatureDTO> signDocument(
            @PathVariable String documentNumber,
            @Valid @RequestBody SignDocumentDTO signDTO) {
        return ResponseEntity.ok(signatureService.signDocument(documentNumber, signDTO));
    }
    
    @GetMapping("/documents/{documentNumber}")
    public ResponseEntity<DigitalDocumentDTO> getDocument(@PathVariable String documentNumber) {
        return ResponseEntity.ok(signatureService.getDocument(documentNumber));
    }
    
    @GetMapping("/documents/{documentNumber}/signatures")
    public ResponseEntity<List<SignatureDTO>> getDocumentSignatures(@PathVariable String documentNumber) {
        return ResponseEntity.ok(signatureService.getDocumentSignatures(documentNumber));
    }
    
    @PostMapping("/documents/{documentNumber}/signatures/{signatureId}/validate")
    public ResponseEntity<ValidationResultDTO> validateSignature(
            @PathVariable String documentNumber,
            @PathVariable String signatureId) {
        signatureService.validateSignature(documentNumber, signatureId);
        return ResponseEntity.ok(new ValidationResultDTO(true, "Signature is valid"));
    }
}
