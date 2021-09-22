package org.kosiuk.webApp.exceptions;

public class NotCompatibleRolesException extends Exception {

    public NotCompatibleRolesException() {
    }

    public NotCompatibleRolesException(String message) {
        super(message);
    }

    public NotCompatibleRolesException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotCompatibleRolesException(Throwable cause) {
        super(cause);
    }

    public NotCompatibleRolesException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
