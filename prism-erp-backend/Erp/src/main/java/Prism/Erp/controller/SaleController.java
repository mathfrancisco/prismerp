package Prism.Erp.controller;

import Prism.Erp.dto.*;
import Prism.Erp.entity.CreateSaleRequest;
import Prism.Erp.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@RequestBody CreateSaleRequest request) {
        return ResponseEntity.ok(saleService.createSale(request));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<SaleDTO> confirmSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.confirmSale(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<SaleDTO> cancelSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.cancelSale(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSale(id));
    }

    @GetMapping
    public ResponseEntity<Page<SaleDTO>> getSales(Pageable pageable) {
        return ResponseEntity.ok(saleService.getSales(pageable));
    }
}