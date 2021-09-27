package org.kosiuk.webApp.util.sumConversion;

public interface MoneyStringOperator {

    String getOperatedSumString();

    default String getOperatedComissionString() {
        return "0.0";
    }

    default String getOperatedTotalString() {return "0.0";}

}
