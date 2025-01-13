package Prism.Erp.service.business.edi;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AS2Protocol implements EdiProtocol {
    @Override
    public void send(String endpoint, String message, Map<String, String> credentials) {
        // Implementação do envio AS2
    }

    @Override
    public String receive(String endpoint, Map<String, String> credentials) {
        // Implementação do recebimento AS2
        return null;
    }
}