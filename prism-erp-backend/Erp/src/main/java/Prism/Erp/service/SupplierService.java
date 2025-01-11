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