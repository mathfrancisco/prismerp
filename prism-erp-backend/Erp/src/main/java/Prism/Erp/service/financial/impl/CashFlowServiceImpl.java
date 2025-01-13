package Prism.Erp.service.financial.impl;

import Prism.Erp.service.financial.CashFlowService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CashFlowServiceImpl implements CashFlowService {

    public CashFlowReport generateReport(LocalDate startDate, LocalDate endDate) {
        // Buscar movimentações
        // Calcular saldos
        // Gerar projeções
        // Consolidar por categoria
        return null;
    }
}
