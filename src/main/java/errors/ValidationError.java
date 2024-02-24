package errors;

import javax.validation.ValidationException;

public class ValidationError extends ValidationException {
    public ValidationError(String message) {
        super(message);
    }
}
