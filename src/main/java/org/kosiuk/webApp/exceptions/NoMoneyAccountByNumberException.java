package org.kosiuk.webApp.exceptions;

public class NoMoneyAccountByNumberException extends Exception{

    public NoMoneyAccountByNumberException() {
    }

    public NoMoneyAccountByNumberException(String message) {
        super(message);
    }

    public NoMoneyAccountByNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMoneyAccountByNumberException(Throwable cause) {
        super(cause);
    }

    public NoMoneyAccountByNumberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
