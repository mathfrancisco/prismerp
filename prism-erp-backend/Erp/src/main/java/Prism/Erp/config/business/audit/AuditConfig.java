package Prism.Erp.config.business.audit;

import Prism.Erp.model.Address;
import Prism.Erp.service.business.audit.AuditAspect;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AuditConfig {

    @Bean
    public AuditAspect auditAspect(
            AuditService auditService,
            ObjectMapper objectMapper,
            HttpServletRequest request) {
        return new AuditAspect(auditService, objectMapper, request);
    }

    @Bean
    public JaversBuilder javersBuilder() {
        return JaversBuilder.javers()
                .registerValueObject(new ValueObjectDefinition(Money.class, "amount", "currency"))
                .registerValueObject(new ValueObjectDefinition(Address.class, "street", "city", "country"))
                .build();
    }
}
