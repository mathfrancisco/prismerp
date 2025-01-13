@Service
@RequiredArgsConstructor
public class CertificateService {

    private final KeyStore trustStore;
    private final CertificateFactory certificateFactory;
    
    public CertificateInfo validateCertificate(String certificatePem) {
        try {
            X509Certificate cert = loadCertificate(certificatePem);
            
            // Validar período de validade
            cert.checkValidity();
            
            // Validar propósito
            validateKeyUsage(cert);
            
            // Validar revogação
            checkRevocationStatus(cert);
            
            return extractCertificateInfo(cert);
            
        } catch (Exception e) {
            throw new CertificateValidationException("Invalid certificate: " + e.getMessage());
        }
    }
    
    public void validateCertificateChain(String certificateInfo) {
        try {
            // Carregar cadeia de certificados
            List<X509Certificate> chain = loadCertificateChain(certificateInfo);
            
            // Construir e validar caminho de certificação
            CertPathValidator validator = CertPathValidator.getInstance("PKIX");
            PKIXParameters params = new PKIXParameters(trustStore);
            
            CertPath certPath = certificateFactory.generateCertPath(chain);
            validator.validate(certPath, params);
            
        } catch (Exception e) {
            throw new CertificateChainValidationException("Invalid certificate chain: " + e.getMessage());
        }
    }
}
