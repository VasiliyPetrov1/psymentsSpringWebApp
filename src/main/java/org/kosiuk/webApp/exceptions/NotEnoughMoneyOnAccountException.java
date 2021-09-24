package org.kosiuk.webApp.exceptions;

public class NotEnoughMoneyOnAccountException extends Exception{

    private String payedSumString;
    private String paymentComissionString;

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

    public String getPayedSumString() {
        return payedSumString;
    }

    public void setPayedSumString(String payedSumString) {
        this.payedSumString = payedSumString;
    }

    public String getPaymentComissionString() {
        return paymentComissionString;
    }

    public void setPaymentComissionString(String paymentComissionString) {
        this.paymentComissionString = paymentComissionString;
    }
}
