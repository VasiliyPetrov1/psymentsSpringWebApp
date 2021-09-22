package org.kosiuk.webApp.exceptions;

public class ToOwnRequisitePaymentException extends Exception{

    public ToOwnRequisitePaymentException() {
    }

    public ToOwnRequisitePaymentException(String message) {
        super(message);
    }

    public ToOwnRequisitePaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToOwnRequisitePaymentException(Throwable cause) {
        super(cause);
    }

    public ToOwnRequisitePaymentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
