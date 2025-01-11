public enum PurchaseOrderStatus {
    DRAFT,
    PENDING_APPROVAL,
    APPROVED,
    REJECTED,
    SENT_TO_SUPPLIER,
    CONFIRMED_BY_SUPPLIER,
    PARTIALLY_RECEIVED,
    FULLY_RECEIVED,
    CANCELLED,
    COMPLETED;
    
    public boolean isTerminalState() {
        return this == COMPLETED || this == CANCELLED;
    }
    
    public boolean requiresApproval() {
        return this == PENDING_APPROVAL;
    }
}