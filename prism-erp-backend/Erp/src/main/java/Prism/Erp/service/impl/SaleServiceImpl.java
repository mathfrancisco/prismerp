package Prism.Erp.service.impl;

import Prism.Erp.dto.*;
import Prism.Erp.entity.*;
import Prism.Erp.exception.*;
import Prism.Erp.service.mapper.SaleMapper;
import Prism.Erp.model.SaleStatus;
import Prism.Erp.repository.*;
import Prism.Erp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final InventorySalesIntegrationService inventoryIntegrationService;
    private final InvoiceService invoiceService;
    private final SaleMapper saleMapper;

    @Autowired
    public SaleServiceImpl(
            SaleRepository saleRepository,
            ProductRepository productRepository,
            InventorySalesIntegrationService inventoryIntegrationService,
            InvoiceService invoiceService,
            SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.inventoryIntegrationService = inventoryIntegrationService;
        this.invoiceService = invoiceService;
        this.saleMapper = saleMapper;
    }

    @Override
    public SaleDTO createSale(CreateSaleRequest request) {
        // Validar disponibilidade de estoque
        List<SalesOrderItem> orderItems = request.getItems().stream()
                .map(item -> convertToSalesOrderItem(item))
                .collect(Collectors.toList());

        inventoryIntegrationService.validateStockAvailability(orderItems);

        // Criar venda
        Sale sale = saleMapper.toEntity(request);
        sale.setStatus(SaleStatus.DRAFT);
        sale.setSaleDate(LocalDateTime.now());
        sale = saleRepository.save(sale);

        // Reservar estoque
        inventoryIntegrationService.reserveInventory(sale.getId(), orderItems);

        return saleMapper.toDto(sale);
    }

    @Override
    @Transactional
    public SaleDTO confirmSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (sale.getStatus() != SaleStatus.DRAFT) {
            throw new BusinessException("Sale can only be confirmed from DRAFT status");
        }

        // Processar transações de estoque
        inventoryIntegrationService.processSaleInventoryTransaction(saleMapper.toDto(sale));

        // Gerar fatura
        InvoiceDTO invoice = invoiceService.generateInvoice(saleId);

        // Atualizar status da venda
        sale.setStatus(SaleStatus.CONFIRMED);
        sale = saleRepository.save(sale);

        return saleMapper.toDto(sale);
    }

    @Override
    @Transactional
    public SaleDTO cancelSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        if (sale.getStatus() == SaleStatus.CANCELLED) {
            throw new BusinessException("Sale is already cancelled");
        }

        if (sale.getStatus() == SaleStatus.COMPLETED) {
            throw new BusinessException("Cannot cancel a completed sale");
        }

        // Se a venda estava confirmada, reverter transações de inventário
        if (sale.getStatus() == SaleStatus.CONFIRMED) {
            inventoryIntegrationService.cancelSaleInventoryTransaction(
                    saleId,
                    sale.getItems().stream()
                            .map(saleMapper::toItemDto)
                            .collect(Collectors.toList())
            );
        }

        sale.setStatus(SaleStatus.CANCELLED);
        Sale savedSale = saleRepository.save(sale);

        return saleMapper.toDto(savedSale);
    }

    @Override
    public Page<SaleDTO> getSales(Pageable pageable) {
        return saleRepository.findAll(pageable)
                .map(saleMapper::toDto);
    }

    @Override
    public SaleDTO getSale(Long id) {
        return saleRepository.findById(id)
                .map(saleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
    }

    private SalesOrderItem convertToSalesOrderItem(CreateSaleItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        SalesOrderItem orderItem = new SalesOrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(request.getQuantity());
        return orderItem;
    }
}