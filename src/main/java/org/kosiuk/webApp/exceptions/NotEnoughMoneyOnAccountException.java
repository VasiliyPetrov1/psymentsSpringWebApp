package org.kosiuk.webApp.exceptions;

public class NotEnoughMoneyOnAccountException extends Exception{

    private double payedSum;
    private double paymentComission;

    public NotEnoughMoneyOnAccountException() {
    }

    public NotEnoughMoneyOnAccountException(String message) {
        super(message);
    }

    public NotEnoughMoneyOnAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughMoneyOnAccountException(Throwable cause) {
        super(cause);
    }

    public NotEnoughMoneyOnAccountException(String message, Throwable cause,
                                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public double getPayedSum() {
        return payedSum;
    }

    public void setPayedSum(double payedSum) {
        this.payedSum = payedSum;
    }

    public double getPaymentComission() {
        return paymentComission;
    }

    public void setPaymentComission(double paymentComission) {
        this.paymentComission = paymentComission;
    }
}
