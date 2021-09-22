package org.kosiuk.webApp.util.validator;

public class NotBlankValidator implements Validator<String> {
    private final String message;

    public NotBlankValidator(String message) {
        this.message = message;
    }

    @Override
    public Result validate(String value) {
        if (!value.isBlank()) {
            return new SimpleResult(true);
        } else {
            return new SimpleResult(false, message);
        }
    }
}
