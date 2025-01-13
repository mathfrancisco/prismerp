@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DigitalSignatureService {

    private final DigitalDocumentRepository documentRepository;
    private final DigitalSignatureRepository signatureRepository;
    private final DocumentStorageService storageService;
    private final CertificateService certificateService;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public DigitalDocumentDTO createDocument(CreateDocumentDTO createDTO) {
        // Validar e salvar documento
        byte[] content = Base64.getDecoder().decode(createDTO.getContent());
        String contentHash = calculateHash(content);
        
        DigitalDocument document = new DigitalDocument();
        document.setTitle(createDTO.getTitle());
        document.setType(createDTO.getType());
        document.setDocumentNumber(generateDocumentNumber(createDTO.getType()));
        document.setStatus(DocumentStatus.DRAFT);
        document.setContentHash(contentHash);
        document.setCreatedAt(LocalDateTime.now());
        document.setExpiresAt(createDTO.getExpiresAt());
        document.setRequiredSigners(createDTO.getRequiredSigners());
        
        // Armazenar documento
        String location = storageService.storeDocument(content, document.getDocumentNumber());
        document.setStorageLocation(location);
        
        DigitalDocument savedDocument = documentRepository.save(document);
        
        // Notificar signatários
        if (createDTO.isNotifySigners()) {
            notifySigners(savedDocument);
        }
        
        return modelMapper.map(savedDocument, DigitalDocumentDTO.class);
    }
    
    @Transactional
    public SignatureDTO signDocument(String documentNumber, SignDocumentDTO signDTO) {
        DigitalDocument document = documentRepository.findByDocumentNumber(documentNumber)
            .orElseThrow(() -> new DocumentNotFoundException(documentNumber));
            
        // Validar estado do documento
        validateDocumentStatus(document);
        
        // Validar certificado
        CertificateInfo certInfo = certificateService.validateCertificate(signDTO.getCertificate());
        
        // Recuperar conteúdo do documento
        byte[] content = storageService.retrieveDocument(document.getStorageLocation());
        
        // Validar hash do documento
        validateDocumentHash(content, document.getContentHash());
        
        // Criar assinatura
        DigitalSignature signature = new DigitalSignature();
        signature.setDocument(document);
        signature.setSignerName(certInfo.getSubjectName());
        signature.setSignerEmail(certInfo.getSubjectEmail());
        signature.setSignatureHash(calculateSignatureHash(content, signDTO.getSignature()));
        signature.setCertificateInfo(certInfo.toString());
        signature.setSignedAt(LocalDateTime.now());
        signature.setIpAddress(RequestContextHolder.currentRequestAttributes().getRemoteAddress());
        signature.setMetadata(signDTO.getMetadata());
        
        DigitalSignature savedSignature = signatureRepository.save(signature);
        
        // Atualizar status do documento
        updateDocumentStatus(document);
        
        // Registrar auditoria
        auditService.logSignature(document, savedSignature);
        
        return modelMapper.map(savedSignature, SignatureDTO.class);
    }
    
    public void validateSignature(String documentNumber, String signatureId) {
        DigitalDocument document = documentRepository.findByDocumentNumber(documentNumber)
            .orElseThrow(() -> new DocumentNotFoundException(documentNumber));
            
        DigitalSignature signature = signatureRepository.findById(signatureId)
            .orElseThrow(() -> new SignatureNotFoundException(signatureId));
            
        // Recuperar conteúdo e validar hashes
        byte[] content = storageService.retrieveDocument(document.getStorageLocation());
        validateDocumentHash(content, document.getContentHash());
        validateSignatureHash(content, signature.getSignatureHash());
        
        // Validar certificado
        certificateService.validateCertificateChain(signature.getCertificateInfo());
    }
}
