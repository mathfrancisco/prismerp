package Prism.Erp.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
}