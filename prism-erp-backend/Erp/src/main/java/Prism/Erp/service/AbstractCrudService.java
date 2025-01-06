package Prism.Erp.service;

import Prism.Erp.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractCrudService<T extends BaseEntity, D> {

    protected final JpaRepository<T, Long> repository;

    protected AbstractCrudService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public abstract D convertToDTO(T entity);

    public abstract T convertToEntity(D dto);

    @Transactional(readOnly = true)
    public Page<D> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public D findById(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    }

    public D create(D dto) {
        T entity = convertToEntity(dto);
        return convertToDTO(repository.save(entity));
    }

    public D update(Long id, D dto) {
        return repository.findById(id)
                .map(entity -> {
                    T updated = convertToEntity(dto);
                    updated.setId(id);
                    return convertToDTO(repository.save(updated));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
