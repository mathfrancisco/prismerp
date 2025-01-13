package Prism.Erp.service.business.supplier;

import Prism.Erp.dto.business.supplier.SupplierContactDTO;
import Prism.Erp.dto.business.supplier.SupplierDTO;
import Prism.Erp.dto.business.supplier.SupplierDocumentDTO;
import Prism.Erp.dto.business.supplier.SupplierEvaluationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface SupplierService {
    SupplierDTO createSupplier(SupplierDTO supplierDTO);
    SupplierDTO findById(Long id);
    Page<SupplierDTO> findAll(Pageable pageable);
    SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO);
    void deleteSupplier(Long id);
    void addDocument(Long id, SupplierDocumentDTO documentDTO);
    void addContactRecord(Long id, SupplierContactDTO contactDTO);
    List<SupplierDTO> findQualifiedSuppliers(BigDecimal minRating);
    SupplierEvaluationDTO evaluateSupplier(Long id);
}