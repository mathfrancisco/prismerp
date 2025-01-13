package Prism.Erp.service.financial.impl;

import Prism.Erp.service.financial.CostCenterService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CostCenterServiceImpl implements CostCenterService {

    public CostCenterReport generateReport(Long costCenterId, LocalDate period) {
        // Buscar lançamentos
        // Calcular realizados vs orçado
        // Análise de variações
        // Gerar relatório
        return null;
    }
}