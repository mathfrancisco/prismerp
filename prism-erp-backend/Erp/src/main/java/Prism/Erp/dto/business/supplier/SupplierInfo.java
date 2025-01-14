package Prism.Erp.dto.business.supplier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierInfo {
    private String name;
    private String bankAccount;
    private String bankBranch;
    private String documentNumber;
    private String bankCode;
}
