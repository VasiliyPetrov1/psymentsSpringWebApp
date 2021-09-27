package org.kosiuk.webApp.util.sumConversion;

public interface MoneyIntDecOperator {

    long getOperatedSumInt();

    int getOperatedSumDec();

    default long getOperatedComissionInt() {
        return 0L;
    }

    default int getOperatedComissionDec() {
        return 0;
    }

    default long getOperatedTotalInt() {return 0L;}

    default int getOperatedTotalDec() {return 0;}

}
