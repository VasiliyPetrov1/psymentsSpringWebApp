package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class CardPaymentConfirmationDto {

    private Integer senderMoneyAccountId;
    private Long receiverCreditCardNumber;
    private String receiverAccountName;
    private Double payedSum;
    private String assignment;
    private Double paymentComission;

    public CardPaymentConfirmationDto(Integer senderMoneyAccountId, Long receiverCreditCardNumber,
                                      String receiverAccountName, Double payedSum, String assignment, Double paymentComission) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.receiverCreditCardNumber = receiverCreditCardNumber;
        this.receiverAccountName = receiverAccountName;
        this.payedSum = payedSum;
        this.assignment = assignment;
        this.paymentComission = paymentComission;
    }

    public CardPaymentConfirmationDto() {
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

    public String getReceiverAccountName() {
        return receiverAccountName;
    }

    public void setReceiverAccountName(String receiverAccountName) {
        this.receiverAccountName = receiverAccountName;
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

    public Double getPaymentComission() {
        return paymentComission;
    }

    public void setPaymentComission(Double paymentComission) {
        this.paymentComission = paymentComission;
    }
}
