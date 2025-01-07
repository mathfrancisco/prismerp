package Prism.Erp.controller;

import Prism.Erp.dto.SalesOrderDTO;
import Prism.Erp.model.OrderStatus;
import Prism.Erp.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<SalesOrderDTO> createOrder(@RequestBody SalesOrderDTO orderDTO) {
        return ResponseEntity.ok(salesOrderService.createOrder(orderDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SalesOrderDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(salesOrderService.updateStatus(id, status));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<SalesOrderDTO>> getCustomerOrders(
            @PathVariable Long customerId,
            Pageable pageable) {
        return ResponseEntity.ok(salesOrderService.getCustomerOrders(customerId, pageable));
    }
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<SalesOrderDTO> getByOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(salesOrderService.getByOrderNumber(orderNumber));
    }
}
