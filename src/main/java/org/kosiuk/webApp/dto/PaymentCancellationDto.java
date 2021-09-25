package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.sumConversion.MoneyStringOperator;
import org.springframework.stereotype.Component;

@Component
public class PaymentCancellationDto implements MoneyStringOperator {

    Integer senderMoneyAccountId;
    Long paymentNumber;
    String payedSumString;

    public PaymentCancellationDto(Integer senderMoneyAccountId, Long paymentNumber,
                                  String payedSumString) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.paymentNumber = paymentNumber;
        this.payedSumString = payedSumString;
    }

    public PaymentCancellationDto() {
    }

    public Integer getSenderMoneyAccountId() {
        return senderMoneyAccountId;
    }

    public void setSenderMoneyAccountId(Integer senderMoneyAccountId) {
        this.senderMoneyAccountId = senderMoneyAccountId;
    }

    public Long getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Long paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getPayedSumString() {
        return payedSumString;
    }

    public void setPayedSumString(String payedSumString) {
        this.payedSumString = payedSumString;
    }

    @Override
    public String getOperatedSumString() {
        return getPayedSumString();
    }

}
