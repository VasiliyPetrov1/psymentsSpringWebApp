package org.kosiuk.webApp.util.sumConversion;

public class MoneyIntDecToStringAdapter implements MoneyStringOperator {

    private MoneyIntDecOperator moneyIntDecOperator;

    public MoneyIntDecToStringAdapter(MoneyIntDecOperator moneyIntDecOperator) {
        this.moneyIntDecOperator = moneyIntDecOperator;
    }

    @Override
    public String getOperatedSumString() {
        StringBuilder sumStringSb = new StringBuilder(String.valueOf(moneyIntDecOperator.getOperatedSumInt()));
        sumStringSb.append(".");
        if (moneyIntDecOperator.getOperatedSumDec() < 10) {
            sumStringSb.append("0");
        }
        sumStringSb.append(moneyIntDecOperator.getOperatedSumDec());
        return sumStringSb.toString();
    }

    @Override
    public String getOperatedComissionString() {
        StringBuilder comissionStringSb = new StringBuilder(String.valueOf(moneyIntDecOperator.getOperatedComissionInt()));
        comissionStringSb.append(".");
        if (moneyIntDecOperator.getOperatedComissionDec() < 10) {
            comissionStringSb.append("0");
        }
        comissionStringSb.append(moneyIntDecOperator.getOperatedComissionDec());
        return comissionStringSb.toString();
    }
}
