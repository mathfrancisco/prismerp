package Prism.Erp.model.business;

public enum ApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED,
    RETURNED_FOR_REVISION;
    
    public boolean isFinal() {
        return this == APPROVED || this == REJECTED;
    }
}