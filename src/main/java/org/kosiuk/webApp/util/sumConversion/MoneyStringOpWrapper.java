package org.kosiuk.webApp.util.sumConversion;

public class MoneyStringOpWrapper implements MoneyStringOperator{

    private String sumString;
    private String comissionString;
    private String totalString;

    @Override
    public String getOperatedSumString() {
        return sumString;
    }

    @Override
    public String getOperatedComissionString() {
        return comissionString;
    }

    @Override
    public String getOperatedTotalString() {
        return totalString;
    }

    public void setSumString(String sumString) {
        this.sumString = sumString;
    }

    public void setComissionString(String comissionString) {
        this.comissionString = comissionString;
    }

    public void setTotalString(String totalString) {
        this.totalString = totalString;
    }

    public static MoneyStringOpWrapper.Builder builder() {
        return new MoneyStringOpWrapper.Builder();
    }

    public static class Builder {

        private String sumString;
        private String comissionString;
        private String totalString;

        public MoneyStringOpWrapper.Builder sumString(String sumString) {
            this.sumString = sumString;
            return this;
        }

        public MoneyStringOpWrapper.Builder comissionString(String comissionString) {
            this.comissionString = comissionString;
            return this;
        }

        public MoneyStringOpWrapper.Builder totalString(String totalString) {
            this.totalString = totalString;
            return this;
        }

        public MoneyStringOpWrapper build() {
            MoneyStringOpWrapper moneyStringOpWrapper = new MoneyStringOpWrapper();
            moneyStringOpWrapper.setSumString(sumString);
            moneyStringOpWrapper.setComissionString(comissionString);
            moneyStringOpWrapper.setTotalString(totalString);
            return moneyStringOpWrapper;
        }

    }
}
