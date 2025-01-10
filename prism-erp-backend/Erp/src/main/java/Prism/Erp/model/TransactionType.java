package Prism.Erp.model;

import lombok.Getter;

@Getter
public enum TransactionType {
    INBOUND(false),
    OUTBOUND(true),
    PURCHASE(false),
    SALE(true),
    ADJUSTMENT(false),
    RETURN(false),
    TRANSFER(true),
    WRITE_OFF(true),
    RESERVE(false),
    RELEASE(false),
    REVERSAL(false),
    PAYMENT(true);

    private final boolean decrease;

    TransactionType(boolean decrease) {
        this.decrease = decrease;
    }

    public boolean isDecrease() {
        return decrease;
    }
}