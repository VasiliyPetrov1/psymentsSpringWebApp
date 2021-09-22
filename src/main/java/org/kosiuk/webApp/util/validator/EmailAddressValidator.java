package org.kosiuk.webApp.util.validator;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailAddressValidator implements Validator<String>{

    private final String message;

    public EmailAddressValidator(String message) {
        this.message = message;
    }

    @Override
    public Result validate(String value) {
        EmailValidator validator = EmailValidator.getInstance();
        if (validator.isValid(value)) {
            return new SimpleResult(true);
        } else {
            return new SimpleResult(false, message);
        }
    }
}
