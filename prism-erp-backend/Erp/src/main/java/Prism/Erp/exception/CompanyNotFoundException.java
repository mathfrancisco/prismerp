package Prism.Erp.exception;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(Long id) {
        super("Empresa não encontrada com ID: " + id);
    }

    public CompanyNotFoundException(String message) {
        super(message);
    }
}