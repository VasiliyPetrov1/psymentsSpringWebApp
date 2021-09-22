package org.kosiuk.webApp.exceptions;

public class NoCreditCardByNumberException extends Exception{

    public NoCreditCardByNumberException() {
    }

    public NoCreditCardByNumberException(String message) {
        super(message);
    }

    public NoCreditCardByNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCreditCardByNumberException(Throwable cause) {
        super(cause);
    }

    public NoCreditCardByNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
