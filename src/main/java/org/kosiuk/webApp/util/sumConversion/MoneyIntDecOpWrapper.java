package org.kosiuk.webApp.util.sumConversion;

public class MoneyIntDecOpWrapper implements MoneyIntDecOperator{

    private long sumInt;
    private int sumDec;
    private long comissionInt;
    private int comissionDec;
    private long totalInt;
    private int totalDec;

    @Override
    public long getOperatedSumInt() {
        return sumInt;
    }

    @Override
    public int getOperatedSumDec() {
        return sumDec;
    }

    @Override
    public long getOperatedComissionInt() {
        return comissionInt;
    }

    @Override
    public int getOperatedComissionDec() {
        return comissionDec;
    }

    @Override
    public long getOperatedTotalInt() {
        return totalInt;
    }

    @Override
    public int getOperatedTotalDec() {
        return totalDec;
    }

    public void setSumInt(long sumInt) {
        this.sumInt = sumInt;
    }

    public void setSumDec(int sumDec) {
        this.sumDec = sumDec;
    }

    public void setComissionInt(long comissionInt) {
        this.comissionInt = comissionInt;
    }

    public void setComissionDec(int comissionDec) {
        this.comissionDec = comissionDec;
    }

    public void setTotalInt(long totalInt) {
        this.totalInt = totalInt;
    }

    public void setTotalDec(int totalDec) {
        this.totalDec = totalDec;
    }

    public static MoneyIntDecOpWrapper.Builder builder() {
        return new MoneyIntDecOpWrapper.Builder();
    }

    public static class Builder {

        private long sumInt;
        private int sumDec;
        private long comissionInt;
        private int comissionDec;
        private long totalInt;
        private int totalDec;

        public MoneyIntDecOpWrapper.Builder sumInt(long sumInt) {
            this.sumInt = sumInt;
            return this;
        }

        public MoneyIntDecOpWrapper.Builder sumDec(int sumDec) {
            this.sumDec = sumDec;
            return this;
        }

        public MoneyIntDecOpWrapper.Builder comissionInt(long comissionInt) {
            this.comissionInt = comissionInt;
            return this;
        }

        public MoneyIntDecOpWrapper.Builder comissionDec(int comissionDec) {
            this.comissionDec = comissionDec;
            return this;
        }

        public MoneyIntDecOpWrapper.Builder totalInt(long totalInt) {
            this.totalInt = totalInt;
            return this;
        }

        public MoneyIntDecOpWrapper.Builder totalDec(int totalDec) {
            this.totalDec = totalDec;
            return this;
        }

        public MoneyIntDecOpWrapper build() {
            MoneyIntDecOpWrapper moneyIntDecOpWrapper = new MoneyIntDecOpWrapper();
            moneyIntDecOpWrapper.setSumInt(sumInt);
            moneyIntDecOpWrapper.setSumDec(sumDec);
            moneyIntDecOpWrapper.setComissionInt(comissionInt);
            moneyIntDecOpWrapper.setComissionDec(comissionDec);
            moneyIntDecOpWrapper.setTotalInt(totalInt);
            moneyIntDecOpWrapper.setTotalDec(totalDec);
            return moneyIntDecOpWrapper;
        }

    }
}
