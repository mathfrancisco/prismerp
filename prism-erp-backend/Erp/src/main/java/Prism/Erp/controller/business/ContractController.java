@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@Validated
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    @PreAuthorize("hasRole('CONTRACT_MANAGER')")
    public ResponseEntity<ContractDTO> createContract(
            @Valid @RequestBody CreateContractDTO createDTO) {
        return ResponseEntity.ok(contractService.createContract(createDTO));
    }
    
    @GetMapping("/{contractNumber}")
    public ResponseEntity<ContractDTO> getContract(@PathVariable String contractNumber) {
        return ResponseEntity.ok(contractService.getContract(contractNumber));
    }
    
    @PutMapping("/{contractNumber}/status")
    @PreAuthorize("hasRole('CONTRACT_MANAGER')")
    public ResponseEntity<ContractDTO> updateContractStatus(
            @PathVariable String contractNumber,
            @Valid @RequestBody UpdateContractStatusDTO updateDTO) {
        return ResponseEntity.ok(contractService.updateContractStatus(contractNumber, updateDTO));
    }
    
    @PostMapping("/{contractNumber}/milestones/{milestoneId}/process")
    @PreAuthorize("hasRole('CONTRACT_MANAGER')")
    public ResponseEntity<Void> processMilestone(
            @PathVariable String contractNumber,
            @PathVariable String milestoneId,
            @Valid @RequestBody ProcessMilestoneDTO processDTO) {
        contractService.processContractMilestone(contractNumber, milestoneId, processDTO);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{contractNumber}/attachments")
    public ResponseEntity<ContractDTO> addAttachment(
            @PathVariable String contractNumber,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(contractService.addAttachment(contractNumber, file));
    }
}
