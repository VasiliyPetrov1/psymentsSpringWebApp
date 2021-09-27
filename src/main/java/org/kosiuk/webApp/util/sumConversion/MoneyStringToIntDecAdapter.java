package org.kosiuk.webApp.util.sumConversion;

public class MoneyStringToIntDecAdapter implements MoneyIntDecOperator {

    private MoneyStringOperator moneyStringOperator;

    public MoneyStringToIntDecAdapter(MoneyStringOperator moneyStringOperator) {
        this.moneyStringOperator = moneyStringOperator;
    }

    @Override
    public long getOperatedSumInt() {
        return Long.parseLong(moneyStringOperator.getOperatedSumString().split("\\.")[0]);
    }

    @Override
    public int getOperatedSumDec() {
        return Integer.parseInt(moneyStringOperator.getOperatedSumString().split("\\.")[1]);
    }

    @Override
    public long getOperatedComissionInt() {
        return Long.parseLong(moneyStringOperator.getOperatedComissionString().split("\\.")[0]);
    }

    @Override
    public int getOperatedComissionDec() {
        return Integer.parseInt(moneyStringOperator.getOperatedComissionString().split("\\.")[1]);
    }

    @Override
    public long getOperatedTotalInt() {
        return Long.parseLong(moneyStringOperator.getOperatedTotalString().split("\\.")[0]);
    }

    @Override
    public int getOperatedTotalDec() {
        return Integer.parseInt(moneyStringOperator.getOperatedTotalString().split("\\.")[1]);
    }
}
