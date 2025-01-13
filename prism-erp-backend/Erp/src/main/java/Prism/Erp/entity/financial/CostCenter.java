package Prism.Erp.entity.financial;

import Prism.Erp.entity.BaseEntity;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class CostCenter extends BaseEntity {
    private String code;
    private String name;
    private String description;
    private CostCenter parent;
    private BigDecimal budget;
    private List<CostCenter> children;
}
