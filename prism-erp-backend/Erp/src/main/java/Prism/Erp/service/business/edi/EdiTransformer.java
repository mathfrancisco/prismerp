package Prism.Erp.service.business.edi;

import java.util.Map;

public interface EdiTransformer {
    Object transform(String rawMessage, Map<String, String> mappings);
    String transformToEdi(Object data, Map<String, String> mappings);
}
