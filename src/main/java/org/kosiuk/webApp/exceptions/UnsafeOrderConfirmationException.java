package org.kosiuk.webApp.exceptions;

public class UnsafeOrderConfirmationException extends Exception{

    public UnsafeOrderConfirmationException() {

    }

    public UnsafeOrderConfirmationException(String message) {
        super(message);
    }

    public UnsafeOrderConfirmationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsafeOrderConfirmationException(Throwable cause) {
        super(cause);
    }

    public UnsafeOrderConfirmationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
