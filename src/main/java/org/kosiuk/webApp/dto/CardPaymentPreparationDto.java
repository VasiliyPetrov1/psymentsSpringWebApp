package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class CardPaymentPreparationDto {

    private Integer senderMoneyAccountId;
    private Long receiverCreditCardNumber;
    private Double payedSum;
    private String assignment;

    public CardPaymentPreparationDto(Integer senderMoneyAccountId, Long receiverCreditCardNumber,
                                     Double payedSum, String assignment) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.receiverCreditCardNumber = receiverCreditCardNumber;
        this.payedSum = payedSum;
        this.assignment = assignment;
    }

    public CardPaymentPreparationDto() {
    }

    public Integer getSenderMoneyAccountId() {
        return senderMoneyAccountId;
    }

    public void setSenderMoneyAccountId(Integer senderMoneyAccountId) {
        this.senderMoneyAccountId = senderMoneyAccountId;
    }

    public Long getReceiverCreditCardNumber() {
        return receiverCreditCardNumber;
    }

    public void setReceiverCreditCardNumber(Long receiverCreditCardNumber) {
        this.receiverCreditCardNumber = receiverCreditCardNumber;
    }

    public Double getPayedSum() {
        return payedSum;
    }

    public void setPayedSum(Double payedSum) {
        this.payedSum = payedSum;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }
}
