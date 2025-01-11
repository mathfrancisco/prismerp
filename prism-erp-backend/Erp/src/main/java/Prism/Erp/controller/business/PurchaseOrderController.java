package Prism.Erp.controller.business;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderAnalyticsService analyticsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        log.info("Creating purchase order: {}", purchaseOrderDTO);
        return ResponseEntity.ok(purchaseOrderService.createPurchaseOrder(purchaseOrderDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> getPurchaseOrder(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseOrderDTO>> getAllPurchaseOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(purchaseOrderService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrder(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        return ResponseEntity.ok(purchaseOrderService.updatePurchaseOrder(id, purchaseOrderDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PurchaseOrderDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody PurchaseOrderStatus status) {
        return ResponseEntity.ok(purchaseOrderService.updateStatus(id, status));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<PurchaseOrderDTO> approvePurchaseOrder(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderApprovalDTO approvalDTO) {
        return ResponseEntity.ok(purchaseOrderService.approvePurchaseOrder(id, approvalDTO));
    }

    @GetMapping("/analytics")
    public ResponseEntity<PurchaseAnalyticsDTO> getPurchaseAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.generateAnalytics(startDate, endDate));
    }
}
