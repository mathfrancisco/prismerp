package Prism.Erp.controller.financial;

import Prism.Erp.dto.financial.PaymentApprovalDTO;
import Prism.Erp.model.financial.ApprovalStatus;
import Prism.Erp.service.financial.PaymentApprovalWorkFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/payment-approvals")
@Slf4j
public class PaymentApprovalController {

    @Autowired
    private PaymentApprovalWorkFlowService workflowService;

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approvePayment(
            @PathVariable Long id,
            @RequestBody PaymentApprovalDTO approvalDTO) {
        log.info("Processing payment approval: {}", id);
        approvalDTO.setId(id);
        approvalDTO.setStatus(ApprovalStatus.APPROVED);
        workflowService.processApproval(approvalDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectPayment(
            @PathVariable Long id,
            @RequestBody PaymentApprovalDTO approvalDTO) {
        log.info("Processing payment rejection: {}", id);
        approvalDTO.setId(id);
        approvalDTO.setStatus(ApprovalStatus.REJECTED);
        workflowService.processApproval(approvalDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PaymentApprovalDTO>> getPendingApprovals() {
        // Implementar busca de aprovações pendentes para o usuário atual
        return ResponseEntity.ok(new ArrayList<>());
    }
}