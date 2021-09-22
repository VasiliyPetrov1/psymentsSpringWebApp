package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class MoneyAccPaymentConfirmationDto {

    private Integer senderMoneyAccountId;
    private Long receiverMoneyAccountNumber;
    private String receiverAccountName;
    private Double payedSum;
    private String assignment;
    private Double paymentComission;

    public MoneyAccPaymentConfirmationDto(Integer senderMoneyAccountId, Long receiverMoneyAccountNumber,
                                          String receiverAccountName, Double payedSum, String assignment,
                                          Double paymentComission) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.receiverMoneyAccountNumber = receiverMoneyAccountNumber;
        this.receiverAccountName = receiverAccountName;
        this.payedSum = payedSum;
        this.assignment = assignment;
        this.paymentComission = paymentComission;
    }

    public MoneyAccPaymentConfirmationDto() {
    }

    public Integer getSenderMoneyAccountId() {
        return senderMoneyAccountId;
    }

    public void setSenderMoneyAccountId(Integer senderMoneyAccountId) {
        this.senderMoneyAccountId = senderMoneyAccountId;
    }

    public Long getReceiverMoneyAccountNumber() {
        return receiverMoneyAccountNumber;
    }

    public void setReceiverMoneyAccountNumber(Long receiverMoneyAccountNumber) {
        this.receiverMoneyAccountNumber = receiverMoneyAccountNumber;
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
