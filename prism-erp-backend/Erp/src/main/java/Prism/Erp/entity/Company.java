package Prism.Erp.entity;

import Prism.Erp.model.Address;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "document_number", unique = true)
    private String documentNumber;

    private String email;
    private String phone;

    @Embedded
    private Address address;

    private Boolean active;
}
