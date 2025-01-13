@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ContractService {

    private final ContractRepository contractRepository;
    private final SupplierRepository supplierRepository;
    private final ContractItemRepository itemRepository;
    private final ContractMilestoneRepository milestoneRepository;
    private final StorageService storageService;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public ContractDTO createContract(CreateContractDTO createDTO) {
        Contract contract = new Contract();
        contract.setContractNumber(generateContractNumber());
        contract.setTitle(createDTO.getTitle());
        contract.setDescription(createDTO.getDescription());
        contract.setSupplier(supplierRepository.findById(createDTO.getSupplierId())
            .orElseThrow(() -> new SupplierNotFoundException(createDTO.getSupplierId())));
        contract.setType(createDTO.getType());
        contract.setStatus(ContractStatus.DRAFT);
        contract.setStartDate(createDTO.getStartDate());
        contract.setEndDate(createDTO.getEndDate());
        contract.setTotalValue(calculateTotalValue(createDTO.getItems()));
        contract.setCurrency(createDTO.getCurrency());
        
        // Processar itens
        List<ContractItem> items = createDTO.getItems().stream()
            .map(itemDTO -> createContractItem(contract, itemDTO))
            .collect(Collectors.toList());
        contract.setItems(items);
        
        // Processar marcos
        List<ContractMilestone> milestones = createDTO.getMilestones().stream()
            .map(milestoneDTO -> createContractMilestone(contract, milestoneDTO))
            .collect(Collectors.toList());
        contract.setMilestones(milestones);
        
        Contract savedContract = contractRepository.save(contract);
        
        // Processar anexos
        if (createDTO.getAttachments() != null) {
            processAttachments(savedContract, createDTO.getAttachments());
        }
        
        // Notificar partes interessadas
        notifyContractCreated(savedContract);
        
        // Registrar auditoria
        auditService.logContractCreation(savedContract);
        
        return modelMapper.map(savedContract, ContractDTO.class);
    }
    
    @Transactional
    public ContractDTO updateContractStatus(String contractNumber, UpdateContractStatusDTO updateDTO) {
        Contract contract = findContractByNumber(contractNumber);
        
        ContractStatus oldStatus = contract.getStatus();
        contract.setStatus(updateDTO.getNewStatus());
        
        if (updateDTO.getNewStatus() == ContractStatus.ACTIVE) {
            validateContractActivation(contract);
        }
        
        Contract updatedContract = contractRepository.save(contract);
        
        // Notificar mudança de status
        notifyStatusChange(updatedContract, oldStatus);
        
        // Registrar auditoria
        auditService.logContractStatusChange(updatedContract, oldStatus, updateDTO.getNewStatus());
        
        return modelMapper.map(updatedContract, ContractDTO.class);
    }
    
    public void processContractMilestone(String contractNumber, String milestoneId, ProcessMilestoneDTO processDTO) {
        Contract contract = findContractByNumber(contractNumber);
        ContractMilestone milestone = findMilestone(contract, milestoneId);
        
        milestone.setStatus(processDTO.getStatus());
        milestone.setCompletedDate(LocalDate.now());
        milestone.setCompletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        milestone.setNotes(processDTO.getNotes());
        
        milestoneRepository.save(milestone);
        
        // Verificar progresso geral do contrato
        checkContractProgress(contract);
        
        // Notificar conclusão do marco
        notifyMilestoneCompletion(milestone);
    }
}
