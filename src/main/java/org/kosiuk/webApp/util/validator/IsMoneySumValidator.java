package org.kosiuk.webApp.util.validator;

import org.kosiuk.webApp.constants.Regex;

public class IsMoneySumValidator implements Validator<String>{
    private final String message;

    public IsMoneySumValidator(String message) {
        this.message = message;
    }

    @Override
    public Result validate(String moneySumString) {
        if(moneySumString.matches(Regex.MONEY_SUM_REGEX)) {
            return new SimpleResult(true);
        } else {
            return new SimpleResult(false, message);
        }
    }
}
