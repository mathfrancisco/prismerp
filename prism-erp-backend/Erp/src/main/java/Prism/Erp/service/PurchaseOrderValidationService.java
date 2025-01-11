@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderValidationService {
    private final SupplierService supplierService;
    private final ProductService productService;

    public void validateNewPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {
        validateSupplier(purchaseOrderDTO.getSupplierId());
        validateItems(purchaseOrderDTO.getItems());
        validateDates(purchaseOrderDTO);
    }

    public void validateUpdatePurchaseOrder(PurchaseOrder existingOrder, PurchaseOrderDTO updateDTO) {
        if (existingOrder.getStatus().isTerminalState()) {
            throw new PurchaseOrderException.InvalidOrderStatusException(
                "Cannot update order in terminal state: " + existingOrder.getStatus());
        }
        validateItems(updateDTO.getItems());
    }

    public void validateStatusTransition(PurchaseOrder order, PurchaseOrderStatus newStatus) {
        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new PurchaseOrderException.InvalidOrderStatusException(
                String.format("Invalid status transition from %s to %s", order.getStatus(), newStatus));
        }
    }

    public void validateApproval(PurchaseOrder order, PurchaseOrderApprovalDTO approvalDTO) {
        if (order.getStatus() != PurchaseOrderStatus.PENDING_APPROVAL) {
            throw new PurchaseOrderException.InvalidApprovalException(
                "Order is not in PENDING_APPROVAL status");
        }
    }

    private boolean isValidStatusTransition(PurchaseOrderStatus currentStatus, PurchaseOrderStatus newStatus) {
        // Implementation of status transition rules
        return true; // Simplified for brevity
    }