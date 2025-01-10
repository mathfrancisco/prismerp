package Prism.Erp.entity;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sales_orders")
@Data
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class SalesOrder extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL)
    private List<SalesOrderItem> items;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private LocalDate approvalDate;

    @Setter
    @Column(name = "tax_amount")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "payment_plan_id")
    private PaymentPlan paymentPlan;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payment_status")
    private String paymentStatus;

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }
}