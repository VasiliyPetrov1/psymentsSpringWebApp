package org.kosiuk.webApp.util.validator;

/**
 * Component validator
 */
public interface Validator<T> {
    /**
     * @param value Value that need to be validated
     * @return result of validation
     */
    Result validate(T value);
}
