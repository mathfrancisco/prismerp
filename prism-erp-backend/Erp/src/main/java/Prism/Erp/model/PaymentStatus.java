public enum PaymentStatus {
    PENDING,
    SCHEDULED,
    PARTIALLY_PAID,
    PAID,
    OVERDUE,
    CANCELLED;
    
    public boolean requiresAction() {
        return this == PENDING || this == OVERDUE;
    }
}