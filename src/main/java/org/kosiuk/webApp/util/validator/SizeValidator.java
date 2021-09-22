package org.kosiuk.webApp.util.validator;

public class SizeValidator implements Validator<String>{

    private final int minSize;
    private final int maxSize;
    private final String message;

    public SizeValidator(int minSize, int maxSize, String message) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.message = message;
    }

    @Override
    public Result validate(String value) {
        if (value.length() >= minSize && value.length() <= maxSize) {
            return new SimpleResult(true);
        } else {
            return new SimpleResult(false, message);
        }
    }
}

