package org.kosiuk.webApp.exceptions;

public class BlockedAccountException extends Exception{
    public BlockedAccountException() {
    }

    public BlockedAccountException(String message) {
        super(message);
    }

    public BlockedAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockedAccountException(Throwable cause) {
        super(cause);
    }

    public BlockedAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
