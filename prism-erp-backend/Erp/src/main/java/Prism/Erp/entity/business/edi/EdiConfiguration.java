package Prism.Erp.entity.business.edi;

import Prism.Erp.entity.business.Tenant.TenantAwareEntity;
import Prism.Erp.entity.business.supplier.Supplier;
import Prism.Erp.model.business.EdiFormat;
import Prism.Erp.model.business.EdiProtocol;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "edi_configurations")
@Getter
@Setter
public class EdiConfiguration extends TenantAwareEntity {

    @OneToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EdiFormat format;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EdiProtocol protocol;

    @Column(nullable = false)
    private String endpoint;

    @Convert(converter = JsonConverter.class)
    private Map<String, String> credentials;

    @Convert(converter = JsonConverter.class)
    private Map<String, String> mappings;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime lastSync;

    private String certificatePath;
}
