package Prism.Erp.repository;

import Prism.Erp.entity.PaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long>,
        JpaSpecificationExecutor<PaymentPlan> {
}