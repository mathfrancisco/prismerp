package Prism.Erp.controller.business;

import Prism.Erp.dto.business.inventory.InventoryItemDTO;
import Prism.Erp.dto.business.inventory.StockAdjustmentDTO;
import Prism.Erp.repository.business.inventory.StockReservationService;
import Prism.Erp.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/business/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService inventoryService;
    private final StockReservationService reservationService;

    @GetMapping("/items/{sku}")
    public ResponseEntity<InventoryItemDTO> getItem(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getItemBySku(sku));
    }

    @GetMapping("/items/{sku}/movements")
    public ResponseEntity<List<InventoryMovementDTO>> getItemMovements(
            @PathVariable String sku,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(inventoryService.getItemMovements(sku, startDate, endDate));
    }

    @PostMapping("/items/{sku}/adjust")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryItemDTO> adjustStock(
            @PathVariable String sku,
            @Valid @RequestBody StockAdjustmentDTO adjustment) {
        return ResponseEntity.ok(inventoryService.adjustStock(sku, adjustment));
    }

    @PostMapping("/items/{sku}/reserve")
    public ResponseEntity<Void> reserveStock(
            @PathVariable String sku,
            @Valid @RequestBody StockReservationDTO reservation) {
        reservationService.reserveStock(
                sku,
                reservation.getQuantity(),
                reservation.getReferenceType(),
                reservation.getReferenceId()
        );
        return ResponseEntity.ok().build();
    }
}
