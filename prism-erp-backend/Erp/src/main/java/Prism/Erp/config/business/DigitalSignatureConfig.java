@Configuration
public class DigitalSignatureConfig {
    
    @Bean
    public KeyStore trustStore() throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("truststore.jks"), "password".toCharArray());
        return trustStore;
    }
    
    @Bean
    public CertificateFactory certificateFactory() throws Exception {
        return CertificateFactory.getInstance("X.509");
    }
    
    @Bean
    public SignatureAlgorithm signatureAlgorithm() {
        return SignatureAlgorithm.SHA256withRSA;
    }
}
