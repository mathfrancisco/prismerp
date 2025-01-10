package Prism.Erp.service.impl;


import Prism.Erp.dto.SalesOrderDTO;
import Prism.Erp.dto.SalesOrderItemDTO;
import Prism.Erp.entity.*;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.InsufficientStockException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.OrderStatus;
import Prism.Erp.repository.CustomerRepository;
import Prism.Erp.repository.PaymentPlanRepository;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.repository.SalesOrderRepository;
import Prism.Erp.service.InvoiceService;
import Prism.Erp.service.SalesOrderService;
import Prism.Erp.validation.ValidationService; // Certifique-se de que este serviço exista
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ValidationService validationService;
    private final InventorySalesIntegrationService inventoryService;
    private final InvoiceService invoiceService;
    private final PaymentPlanRepository paymentPlanRepository;


    @Override
    public SalesOrderDTO createOrder(SalesOrderDTO orderDTO) {
        validationService.validate(orderDTO);

        // Validar cliente
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // Verificar limite de crédito do cliente
        validateCustomerCreditLimit(customer, orderDTO.getTotalAmount());

        List<SalesOrderItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Processar itens e validar estoque
        for (SalesOrderItemDTO itemDTO : orderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            // Validar disponibilidade de estoque
            validateProductStock(product.getId(), itemDTO.getQuantity());

            BigDecimal totalPrice = calculateItemPrice(product, itemDTO.getQuantity());
            totalAmount = totalAmount.add(totalPrice);

            items.add(createOrderItem(product, itemDTO.getQuantity(), totalPrice));
        }

        // Criar pedido
        SalesOrder salesOrder = createSalesOrder(customer, items, totalAmount);

        // Reservar estoque
        inventoryService.reserveInventory(salesOrder.getId(), items);

        // Calcular impostos
        BigDecimal taxAmount = invoiceService.calculateTax(salesOrder);
        salesOrder.setTaxAmount(taxAmount);

        SalesOrder savedOrder = salesOrderRepository.save(salesOrder);
        return toDTO(savedOrder);
    }
    @Override
    @Transactional
    public SalesOrderDTO approveOrder(Long id) {
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (order.getStatus() != OrderStatus.PENDING_APPROVAL) {
            throw new BusinessException("Pedido não está pendente de aprovação");
        }

        // Validar novamente o estoque
        validateOrderStock(order);

        // Processar pagamento se necessário
        if (requiresPaymentProcessing(order)) {
            processPayment(order);
        }

        // Atualizar status
        order.setStatus(OrderStatus.APPROVED);
        order.setApprovalDate(LocalDate.now());

        // Gerar fatura
        Invoice invoice = invoiceService.generateInvoice(order);
        order.setInvoice(invoice);

        return toDTO(salesOrderRepository.save(order));
    }


    @Override
    @Transactional
    public SalesOrderDTO cancelOrder(Long id) {
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (!canBeCancelled(order.getStatus())) {
            throw new BusinessException("Pedido não pode ser cancelado no status atual");
        }

        // Liberar reserva de estoque
        inventoryService.releaseInventory(order.getId(), order.getItems());

        // Cancelar pagamento se necessário
        if (order.getPaymentId() != null) {
            cancelPayment(order);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return toDTO(salesOrderRepository.save(order));
    }

    private void validateProductStock(Long productId, int quantity) {
        if (!inventoryService.hasAvailableStock(productId, quantity)) {
            throw new InsufficientStockException("Estoque insuficiente");
        }
    }

    private void validateOrderStock(SalesOrder order) {
        for (SalesOrderItem item : order.getItems()) {
            validateProductStock(item.getProduct().getId(), item.getQuantity());
        }
    }

    private BigDecimal calculateItemPrice(Product product, int quantity) {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .multiply(BigDecimal.ONE.subtract(product.getDiscount()));
    }

    private SalesOrder createSalesOrder(Customer customer, List<SalesOrderItem> items, BigDecimal totalAmount) {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderNumber(generateOrderNumber());
        salesOrder.setCustomer(customer);
        salesOrder.setItems(items);
        salesOrder.setStatus(OrderStatus.PENDING_APPROVAL);
        salesOrder.setTotalAmount(totalAmount);
        salesOrder.setOrderDate(LocalDate.now());

        items.forEach(item -> item.setSalesOrder(salesOrder));

        return salesOrder;
    }
    private SalesOrderItem createOrderItem(Product product, int quantity, BigDecimal totalPrice) {
        SalesOrderItem item = new SalesOrderItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.setTotalPrice(totalPrice);
        return item;
    }

    private boolean requiresPaymentProcessing(SalesOrder order) {
        return order.getPaymentPlan() != null &&
                order.getPaymentPlan().requiresPrePayment();
    }

    private void processPayment(SalesOrder order) {
        // Implementar integração com gateway de pagamento
        try {
            // Processar pagamento
            String paymentId = "PAYMENT-" + UUID.randomUUID().toString();
            order.setPaymentId(paymentId);
            order.setPaymentStatus("APPROVED");
        } catch (Exception e) {
            throw new PaymentProcessingException("Erro ao processar pagamento", e);
        }
    }

    private void cancelPayment(SalesOrder order) {
        // Implementar cancelamento do pagamento
        try {
            // Cancelar pagamento no gateway
            order.setPaymentStatus("CANCELLED");
        } catch (Exception e) {
            throw new PaymentProcessingException("Erro ao cancelar pagamento", e);
        }
    }

    private boolean canBeCancelled(OrderStatus status) {
        return status == OrderStatus.DRAFT ||
                status == OrderStatus.PENDING_APPROVAL;
    }


    private String generateOrderNumber() {
        // Gere um número de pedido único, por exemplo, usando UUID
        return UUID.randomUUID().toString();
    }
    @Override
    public SalesOrderDTO getOrderById(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
        return toDTO(salesOrder);
    }

    @Override
    public SalesOrderDTO updateStatus(Long id, OrderStatus status) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
        validateStatusTransition(salesOrder.getStatus(), status);
        salesOrder.setStatus(status);
        return toDTO(salesOrderRepository.save(salesOrder));
    }
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Implementar regras de transição de status
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new BusinessException("Transição de status inválida");
        }
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        // Implementar lógica de validação de transição
        return true; // Simplificado
    }

    @Override
    public Page<SalesOrderDTO> getCustomerOrders(Long customerId, Pageable pageable) {
        return salesOrderRepository.findByCustomerId(customerId, pageable)
                .map(this::toDTO);
    }


    private SalesOrderDTO toDTO(SalesOrder entity) {
        return SalesOrderDTO.builder()
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())
                .customerId(entity.getCustomer().getId())
                .customerName(entity.getCustomer().getName())
                .items(entity.getItems().stream().map(this::toItemDTO).toList())
                .status(entity.getStatus().name())
                .totalAmount(entity.getTotalAmount())
                .orderDate(entity.getOrderDate())
                .deliveryDate(entity.getDeliveryDate())
                .build();
    }

    private SalesOrderItemDTO toItemDTO(SalesOrderItem item) {
        return SalesOrderItemDTO.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
    @Override
    public SalesOrderDTO getByOrderNumber(String orderNumber) {
        return salesOrderRepository.findByOrderNumber(orderNumber)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de venda não encontrado com o número: " + orderNumber));
    }

}
