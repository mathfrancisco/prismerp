package Prism.Erp.entity.business.supplier;

import Prism.Erp.model.business.SupplierStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Entity
@Table(name = "suppliers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Supplier implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String documentNumber;

    private String contactName;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupplierStatus status;

    @Column(length = 500)
    private String address;

    private String website;

    @Column(length = 500)
    private String bankDetails;

    private Integer paymentTerms;

    private String taxRegime;

    @Column(precision = 19, scale = 4)
    private BigDecimal creditLimit;

    @Column(precision = 5, scale = 2)
    private BigDecimal qualityRating;

    @Column(precision = 5, scale = 2)
    private BigDecimal deliveryPerformance;

    private LocalDate lastEvaluationDate;

    @ElementCollection
    @CollectionTable(name = "supplier_categories")
    private Set<String> categories = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "supplier_certifications")
    private Set<String> certifications = new HashSet<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<SupplierDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<SupplierContact> contactHistory = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;

    @Override
    public void setCreatedBy(Object createdBy) {

    }

    @Override
    public Optional getCreatedDate() {
        return Optional.empty();
    }

    @Override
    public void setCreatedDate(TemporalAccessor creationDate) {

    }

    @Override
    public Optional getLastModifiedBy() {
        return Optional.empty();
    }

    @Override
    public void setLastModifiedBy(Object lastModifiedBy) {

    }

    @Override
    public Optional getLastModifiedDate() {
        return Optional.empty();
    }

    @Override
    public void setLastModifiedDate(TemporalAccessor lastModifiedDate) {

    }

    @Override
    public boolean isNew() {
        return false;
    }
}