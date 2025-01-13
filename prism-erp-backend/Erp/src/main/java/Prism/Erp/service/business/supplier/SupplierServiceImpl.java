package Prism.Erp.service.business.supplier;

import Prism.Erp.dto.business.supplier.SupplierContactDTO;
import Prism.Erp.dto.business.supplier.SupplierDTO;
import Prism.Erp.dto.business.supplier.SupplierDocumentDTO;
import Prism.Erp.dto.business.supplier.SupplierEvaluationDTO;
import Prism.Erp.entity.business.supplier.Supplier;
import Prism.Erp.entity.business.supplier.SupplierContact;
import Prism.Erp.entity.business.supplier.SupplierDocument;
import Prism.Erp.exception.SupplierException;
import Prism.Erp.model.business.SupplierStatus;
import Prism.Erp.repository.business.supplier.SupplierRepository;
import Prism.Erp.service.NotificationService;
import Prism.Erp.service.mapper.business.SupplierMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierValidationService validationService;
    private final SupplierEvaluationService evaluationService;
    private final NotificationService notificationService;

    @Override
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        log.info("Creating new supplier: {}", supplierDTO.getName());

        validationService.validateNewSupplier(supplierDTO);

        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        supplier.setStatus(SupplierStatus.PENDING_APPROVAL);

        Supplier savedSupplier = supplierRepository.save(supplier);
        notificationService.notifySupplierCreated(savedSupplier);

        return supplierMapper.toDTO(savedSupplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierDTO findById(Long id) {
        return supplierRepository.findById(id)
                .map(supplierMapper::toDTO)
                .orElseThrow(() -> new SupplierException.SupplierNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> findAll(Pageable pageable) {
        return supplierRepository.findAll(pageable)
                .map(supplierMapper::toDTO);
    }

    @Override
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        log.info("Updating supplier: {}", id);

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierException.SupplierNotFoundException(id));

        validationService.validateUpdateSupplier(existingSupplier, supplierDTO);

        updateSupplierDetails(existingSupplier, supplierDTO);

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        notificationService.notifySupplierUpdated(updatedSupplier);

        return supplierMapper.toDTO(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierException.SupplierNotFoundException(id));

        // Check for any active contracts or pending orders before deletion
        validationService.validateDeletion(supplier);

        supplier.setStatus(SupplierStatus.INACTIVE);
        supplierRepository.save(supplier);

        notificationService.notifySupplierDeleted(supplier);
    }

    @Override
    public void addDocument(Long id, SupplierDocumentDTO documentDTO) {
        log.info("Adding document to supplier: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierException.SupplierNotFoundException(id));

        validationService.validateDocument(documentDTO);

        SupplierDocument document = supplierMapper.toDocumentEntity(documentDTO);
        document.setSupplier(supplier);
        supplier.getDocuments().add(document);

        supplierRepository.save(supplier);
        notificationService.notifyDocumentAdded(supplier, document);
    }

    @Override
    public void addContactRecord(Long id, SupplierContactDTO contactDTO) {
        log.info("Adding contact record to supplier: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierException.SupplierNotFoundException(id));

        SupplierContact contact = supplierMapper.toContactEntity(contactDTO);
        contact.setSupplier(supplier);
        contact.setContactDate(LocalDateTime.now());
        supplier.getContactHistory().add(contact);

        supplierRepository.save(supplier);
        notificationService.notifyContactRecordAdded(supplier, contact);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> findQualifiedSuppliers(BigDecimal minRating) {
        log.info("Finding qualified suppliers with minimum rating: {}", minRating);

        return supplierRepository.findQualifiedSuppliers(minRating).stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierEvaluationDTO evaluateSupplier(Long id) {
        log.info("Evaluating supplier: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierException.SupplierNotFoundException(id));

        SupplierEvaluationDTO evaluation = evaluationService.evaluateSupplier(supplier);

        // Update supplier with evaluation results
        supplier.setQualityRating(evaluation.getQualityScore());
        supplier.setDeliveryPerformance(evaluation.getDeliveryScore());
        supplier.setLastEvaluationDate(LocalDate.now());

        supplierRepository.save(supplier);
        notificationService.notifySupplierEvaluated(supplier, evaluation);

        return evaluation;
    }

    private void updateSupplierDetails(Supplier existingSupplier, SupplierDTO supplierDTO) {
        existingSupplier.setName(supplierDTO.getName());
        existingSupplier.setContactName(supplierDTO.getContactName());
        existingSupplier.setEmail(supplierDTO.getEmail());
        existingSupplier.setPhone(supplierDTO.getPhone());
        existingSupplier.setAddress(supplierDTO.getAddress());
        existingSupplier.setWebsite(supplierDTO.getWebsite());
        existingSupplier.setBankDetails(supplierDTO.getBankDetails());
        existingSupplier.setPaymentTerms(supplierDTO.getPaymentTerms());
        existingSupplier.setTaxRegime(supplierDTO.getTaxRegime());
        existingSupplier.setCreditLimit(supplierDTO.getCreditLimit());
        existingSupplier.setCategories(new HashSet<>(supplierDTO.getCategories()));
        existingSupplier.setCertifications(new HashSet<>(supplierDTO.getCertifications()));
    }
}