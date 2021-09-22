package org.kosiuk.webApp.util.validator;

/**
 * Component result of validation
 */
public interface Result {
    /**
     * @return if validated value is valid
     */
    boolean isValid();
    /**
     * @return get validation result message
     * (used for custom localisation)
     */
    String getMessage();
}

