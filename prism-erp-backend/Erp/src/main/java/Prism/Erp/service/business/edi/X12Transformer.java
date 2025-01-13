package Prism.Erp.service.business.edi;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class X12Transformer implements EdiTransformer {
    @Override
    public Object transform(String rawMessage, Map<String, String> mappings) {
        // Implementação da transformação X12
        return null;
    }

    @Override
    public String transformToEdi(Object data, Map<String, String> mappings) {
        // Implementação da transformação para X12
        return null;
    }
}
