package org.kosiuk.webApp.util.validator;

import java.util.ArrayList;
import java.util.List;

public class CompositeResult implements Result{

    private final List<Result> resultList = new ArrayList<>();

    /**
     * @return if all embed results are true
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        for (Result result : resultList) {
            if (!result.isValid()) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    /**
     * @return validation result messages as one String
     */
    @Override
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Result result : resultList) {
            if (!result.isValid()) {
                stringBuilder.append(result.getMessage()).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public void addResult(Result result) {
        resultList.add(result);
    }

}
