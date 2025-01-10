package Prism.Erp.service.mapper;

import Prism.Erp.dto.SaleDTO;
import Prism.Erp.dto.SaleItem;
import Prism.Erp.dto.SaleItemDTO;
import Prism.Erp.entity.*;
import Prism.Erp.model.SaleStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class SaleMapper {

    public Sale toEntity(CreateSaleRequest request) {
        Customer customer = new Customer(); // You'll need to fetch this from a repository
        customer.setId(request.getCustomerId());

        Sale sale = Sale.builder()
                .saleNumber(generateSaleNumber())
                .customer(customer)
                .saleDate(LocalDateTime.now())
                .totalAmount(calculateTotalAmount(request))
                .status(SaleStatus.DRAFT)
                .createdBy("SYSTEM") // You might want to get this from security context
                .notes(request.getNotes())
                .build();

        // Set up the items relationship
        if (request.getItems() != null) {
            sale.setItems(request.getItems().stream()
                    .map(item -> convertToSaleItem(item, sale))
                    .collect(Collectors.toList()));
        }

        return sale;
    }

    public SaleDTO toDto(Sale sale) {
        return SaleDTO.builder()
                .id(sale.getId())
                .saleNumber(sale.getSaleNumber())
                .customerId(sale.getCustomer().getId())
                .customerName(sale.getCustomer().getName())
                .saleDate(sale.getSaleDate())
                .totalAmount(sale.getTotalAmount())
                .status(sale.getStatus().name())
                .createdBy(sale.getCreatedBy())
                .notes(sale.getNotes())
                .items(sale.getItems().stream()
                        .map(this::toItemDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public SaleItemDTO toItemDto(SaleItem item) {
        return SaleItemDTO.builder()
                .id(item.getId())
                .saleId(item.getSale().getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .discount(item.getDiscount())
                .build();
    }

    private String generateSaleNumber() {
        return "SALE-" + System.currentTimeMillis();
    }

    private BigDecimal calculateTotalAmount(CreateSaleRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return request.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private SaleItem convertToSaleItem(CreateSaleItemRequest request, Sale sale) {
        Product product = new Product(); // You'll need to fetch this from a repository
        product.setId(request.getProductId());

        return SaleItem.builder()
                .sale(sale)
                .product(product)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .totalPrice(request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .discount(request.getDiscount())
                .build();
    }
}