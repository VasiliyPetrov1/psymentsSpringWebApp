package org.kosiuk.webApp.util.sumConversion;

public interface MoneyStringOperator {

    String getOperatedSumString();

    default String getOperatedComissionString() {
        return "";
    }

}
