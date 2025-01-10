package Prism.Erp.service.impl;

import Prism.Erp.dto.*;
import Prism.Erp.entity.*;
import Prism.Erp.exception.*;
import Prism.Erp.model.SaleStatus;
import Prism.Erp.repository.*;
import Prism.Erp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InventorySalesIntegrationService inventorySalesIntegration;

    @Override
    @Transactional
    public SaleDTO createSale(CreateSaleRequest request) {
        // Buscar cliente
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Criar venda
        Sale sale = Sale.builder()
                .saleNumber(generateSaleNumber())
                .customer(customer)
                .saleDate(LocalDateTime.now())
                .status(SaleStatus.DRAFT)
                .createdBy("SYSTEM") // Deve vir do contexto de segurança
                .notes(request.getNotes())
                .build();

        // Processar itens
        List<SaleItem> items = processItems(sale, request.getItems());
        sale.setItems(items);

        // Calcular total
        BigDecimal total = items.stream()
                .map(SaleItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sale.setTotalAmount(total);

        // Salvar venda
        Sale savedSale = saleRepository.save(sale);

        return convertToDTO(savedSale);
    }

    @Override
    @Transactional
    public SaleDTO confirmSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (sale.getStatus() != SaleStatus.DRAFT) {
            throw new ResourceNotFoundException("Only DRAFT sales can be confirmed");
        }

        // Processar transações de inventário
        inventorySalesIntegration.processSaleInventoryTransaction(convertToDTO(sale));

        // Atualizar status
        sale.setStatus(SaleStatus.CONFIRMED);
        Sale savedSale = saleRepository.save(sale);

        return convertToDTO(savedSale);
    }

    @Override
    @Transactional
    public SaleDTO cancelSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (sale.getStatus() == SaleStatus.CANCELLED) {
            throw new ResourceNotFoundException("Sale is already cancelled");
        }

        if (sale.getStatus() == SaleStatus.COMPLETED) {
            throw new ResourceNotFoundException("Cannot cancel a completed sale");
        }

        // Se a venda estava confirmada, reverter transações de inventário
        if (sale.getStatus() == SaleStatus.CONFIRMED) {
            inventorySalesIntegration.cancelSaleInventoryTransaction(saleId,
                    sale.getItems().stream()
                            .map(this::convertItemToDTO)
                            .collect(Collectors.toList()));
        }

        sale.setStatus(SaleStatus.CANCELLED);
        Sale savedSale = saleRepository.save(sale);

        return convertToDTO(savedSale);
    }

    @Override
    public Page<SaleDTO> getSales(Pageable pageable) {
        return saleRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public SaleDTO getSale(Long id) {
        return saleRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
    }

    private List<SaleItem> processItems(Sale sale, List<CreateSaleItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(request -> {
                    Product product = productRepository.findById(request.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                    BigDecimal unitPrice = product.getPrice();
                    BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
                    BigDecimal totalPrice = unitPrice
                            .multiply(BigDecimal.valueOf(request.getQuantity()))
                            .subtract(discount);

                    return SaleItem.builder()
                            .sale(sale)
                            .product(product)
                            .quantity(request.getQuantity())
                            .unitPrice(unitPrice)
                            .totalPrice(totalPrice)
                            .discount(discount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String generateSaleNumber() {
        return "SALE-" + System.currentTimeMillis();
    }

    private SaleDTO convertToDTO(Sale sale) {
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
                        .map(this::convertItemToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private SaleItemDTO convertItemToDTO(SaleItem item) {
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
}