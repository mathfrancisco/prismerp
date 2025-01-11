public enum SupplierStatus {
    ACTIVE,
    INACTIVE,
    BLOCKED,
    PENDING_APPROVAL,
    UNDER_REVIEW,
    BLACKLISTED;
    
    public boolean canReceiveOrders() {
        return this == ACTIVE;
    }
}