package org.kosiuk.webApp.util.validator;

import java.util.List;

public class NotAllFlagsFalseValidator implements Validator<List<Boolean>>{

    private final String message;

    public NotAllFlagsFalseValidator(String message) {
        this.message = message;
    }

    @Override
    public Result validate(List<Boolean> flags) {
        for(boolean curFlag : flags) {
            if(curFlag) {
                return new SimpleResult(true);
            }
        }
        return new SimpleResult(false, message);
    }
}
