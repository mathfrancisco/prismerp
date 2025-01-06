package Prism.Erp.validation;

import lombok.Value;

@Value
public class ValidationError {
    String field;
    String message;
}
