@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "contactHistory", ignore = true)
    SupplierDTO toDTO(Supplier supplier);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Supplier toEntity(SupplierDTO supplierDTO);

    List<SupplierDTO> toDTOList(List<Supplier> suppliers);

    @Mapping(target = "supplier", ignore = true)
    SupplierDocumentDTO toDocumentDTO(SupplierDocument document);

    @Mapping(target = "supplier", ignore = true)
    SupplierDocument toDocumentEntity(SupplierDocumentDTO documentDTO);

    @Mapping(target = "supplier", ignore = true)
    SupplierContactDTO toContactDTO(SupplierContact contact);

    @Mapping(target = "supplier", ignore = true)
    SupplierContact toContactEntity(SupplierContactDTO contactDTO);

    @AfterMapping
    default void linkDocuments(@MappingTarget Supplier supplier) {
        if (supplier.getDocuments() != null) {
            supplier.getDocuments().forEach(doc -> doc.setSupplier(supplier));
        }
    }

    @AfterMapping
    default void linkContacts(@MappingTarget Supplier supplier) {
        if (supplier.getContactHistory() != null) {
            supplier.getContactHistory().forEach(contact -> contact.setSupplier(supplier));
        }
    }
}