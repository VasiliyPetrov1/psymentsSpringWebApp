package org.kosiuk.webApp.util.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Composite validator that embeds various validators
 */
public class CompositeValidator<T> implements Validator<T> {
    private final List<Validator<T>> validatorList = new ArrayList<>();

    @SafeVarargs
    public CompositeValidator(Validator<T>... validators) {
        this.validatorList.addAll(Arrays.asList(validators));
    }

    /**
     * @return composite result of internal validators
     */
    @Override
    public Result validate(T value) {
        CompositeResult result = new CompositeResult();
        validatorList.stream()
                .map(validator -> validator.validate(value))
                .forEach(result::addResult);
        return result;
    }
}
