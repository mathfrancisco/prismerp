package Prism.Erp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String documentNumber;

    private String email;

    private String phone;

    @Column(name = "discount_rate")
    private BigDecimal discountRate;


    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Embedded
    private RabbitConnectionDetails.Address address;

    @Column(nullable = false)
    private Boolean active = true;
}

