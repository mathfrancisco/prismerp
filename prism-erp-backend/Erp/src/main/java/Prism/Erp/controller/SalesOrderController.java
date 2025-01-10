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
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping("/orders")
    public ResponseEntity<SalesOrderDTO> createOrder(@RequestBody SalesOrderDTO orderDTO) {
        return ResponseEntity.ok(salesOrderService.createOrder(orderDTO));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<SalesOrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.getOrderById(id));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<SalesOrderDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(salesOrderService.updateStatus(id, status));
    }

    @GetMapping("/orders/customer/{customerId}")
    public ResponseEntity<Page<SalesOrderDTO>> getCustomerOrders(
            @PathVariable Long customerId,
            Pageable pageable) {
        return ResponseEntity.ok(salesOrderService.getCustomerOrders(customerId, pageable));
    }
    @GetMapping("/orders/number/{orderNumber}")
    public ResponseEntity<SalesOrderDTO> getByOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(salesOrderService.getByOrderNumber(orderNumber));
    }
}
