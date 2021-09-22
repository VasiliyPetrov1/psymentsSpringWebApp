package org.kosiuk.webApp.util.validator;

public class SimpleResult implements Result {
    private final boolean valid;
    private final String message;

    public SimpleResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public SimpleResult(boolean valid) {
        this.valid = valid;
        this.message = "";
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
