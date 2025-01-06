package Prism.Erp.service.impl;

import Prism.Erp.dto.SalesOrderDTO;
import Prism.Erp.model.SalesOrder;
import Prism.Erp.model.SalesOrderItem;
import Prism.Erp.repository.CustomerRepository;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.repository.SalesOrderRepository;
import Prism.Erp.service.SalesOrderService;
import Prism.Erp.validation.ValidationService;
import Prism.Erp.service.AbstractCrudService;
import Prism.Erp.dto.SalesOrderItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ValidationService validationService;

    @Override
    protected SalesOrderDTO toDTO(SalesOrder entity) {
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
    protected String getEntityName() {
        return "SalesOrder";
    }
}
