package Prism.Erp.controller;

import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.ProductStockDTO;
import Prism.Erp.service.InventoryService;
import Prism.Erp.service.impl.InventoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/transactions")
    public ResponseEntity<InventoryTransactionDTO> createTransaction(
            @RequestBody InventoryTransactionDTO transactionDTO) {
        return ResponseEntity.ok(inventoryService.createTransaction(transactionDTO));
    }

    @GetMapping("/stock-levels")
    public ResponseEntity<Page<ProductStockDTO>> getStockLevels(Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getStockLevels((org.springframework.data.domain.Pageable) pageable));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductStockDTO>> getLowStockProducts() {
        return ResponseEntity.ok(inventoryService.getLowStockProducts());
    }
}
