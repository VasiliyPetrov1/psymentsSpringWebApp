package org.kosiuk.webApp.exceptions;

public class UnsafeMoneyAccCreationException extends Exception{

    public UnsafeMoneyAccCreationException() {
    }

    public UnsafeMoneyAccCreationException(String message) {
        super(message);
    }

    public UnsafeMoneyAccCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsafeMoneyAccCreationException(Throwable cause) {
        super(cause);
    }

    public UnsafeMoneyAccCreationException(String message, Throwable cause,
                                           boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
