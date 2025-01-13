package Prism.Erp.service.business.edi;

import java.util.Map;

public interface EdiProtocol {
    void send(String endpoint, String message, Map<String, String> credentials);
    String receive(String endpoint, Map<String, String> credentials);
}
