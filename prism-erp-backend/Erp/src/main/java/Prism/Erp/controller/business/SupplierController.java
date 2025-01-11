package Prism.Erp.controller.business;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierController {
    private final SupplierService supplierService;
    private final SupplierEvaluationService evaluationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SupplierDTO> createSupplier(
            @Valid @RequestBody SupplierDTO supplierDTO) {
        log.info("Creating supplier: {}", supplierDTO);
        return ResponseEntity.ok(supplierService.createSupplier(supplierDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SupplierDTO>> getAllSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(supplierService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierDTO supplierDTO) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplierDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<Void> addDocument(
            @PathVariable Long id,
            @Valid @RequestBody SupplierDocumentDTO documentDTO) {
        supplierService.addDocument(id, documentDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<Void> addContactRecord(
            @PathVariable Long id,
            @Valid @RequestBody SupplierContactDTO contactDTO) {
        supplierService.addContactRecord(id, contactDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/evaluation")
    public ResponseEntity<SupplierEvaluationDTO> getEvaluation(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.evaluateSupplier(id));
    }
}
