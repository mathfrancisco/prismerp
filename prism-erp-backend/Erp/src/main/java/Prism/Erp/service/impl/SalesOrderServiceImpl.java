package Prism.Erp.service.impl;


import Prism.Erp.dto.SalesOrderDTO;
import Prism.Erp.dto.SalesOrderItemDTO;
import Prism.Erp.entity.Customer;
import Prism.Erp.entity.Product;
import Prism.Erp.entity.SalesOrder;
import Prism.Erp.entity.SalesOrderItem;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.OrderStatus;
import Prism.Erp.repository.CustomerRepository;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.repository.SalesOrderRepository;
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

    @Override
    public SalesOrderDTO createOrder(SalesOrderDTO orderDTO) {
        validationService.validate(orderDTO); // Validação usando ValidationService

        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        List<SalesOrderItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SalesOrderItemDTO itemDTO : orderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(totalPrice);

            SalesOrderItem item = new SalesOrderItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(totalPrice);
            items.add(item);
        }

        String orderNumber = generateOrderNumber(); // Implemente este método

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setOrderNumber(orderNumber);
        salesOrder.setCustomer(customer);
        salesOrder.setItems(items);
        salesOrder.setStatus(OrderStatus.DRAFT); // Ou outro status inicial
        salesOrder.setTotalAmount(totalAmount);
        salesOrder.setOrderDate(LocalDate.now());

        for (SalesOrderItem item : items) {
            item.setSalesOrder(salesOrder); // Associe os itens ao pedido
        }

        SalesOrder savedOrder = salesOrderRepository.save(salesOrder);
        return toDTO(savedOrder);
    }

    private String generateOrderNumber() {
        // Gere um número de pedido único, por exemplo, usando UUID
        return UUID.randomUUID().toString();
    }
    @Override
    public SalesOrderDTO getOrderById(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de venda não encontrado"));
        return toDTO(salesOrder);
    }

    @Override
    public SalesOrderDTO updateStatus(Long id, OrderStatus status) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de venda não encontrado"));
        salesOrder.setStatus(status);
        return toDTO(salesOrderRepository.save(salesOrder));
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

    private String getEntityName() { // Removido @Override e protected
        return "SalesOrder";
    }
}
