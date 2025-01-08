package Prism.Erp.dto;

import lombok.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    private Long id;
    private String name;
    private String documentNumber;
    private String email;
    private String phone;
    private RabbitConnectionDetails.Address address;
    private Boolean active;
}
