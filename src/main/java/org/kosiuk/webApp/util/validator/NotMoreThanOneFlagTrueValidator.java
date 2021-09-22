package org.kosiuk.webApp.util.validator;

import java.util.List;
import java.util.stream.Collectors;

public class NotMoreThanOneFlagTrueValidator implements Validator<List<Boolean>>{

    private final String message;

    public NotMoreThanOneFlagTrueValidator(String message) {
        this.message = message;
    }

    @Override
    public Result validate(List<Boolean> flags) {
        List<Boolean> trueFlagsList = flags.stream().filter(flag -> flag).collect(Collectors.toList());
        if(trueFlagsList.size() == 1 || trueFlagsList.size() == 0) {
            return new SimpleResult(true);
        } else {
            return new SimpleResult(false, message);
        }
    }
}
