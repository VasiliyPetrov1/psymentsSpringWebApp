package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class MoneyAccPaymentPreparationDto {

    private Integer senderMoneyAccountId;
    private Long receiverMoneyAccountNumber;
    private Double payedSum;
    private String assignment;

    public MoneyAccPaymentPreparationDto(Integer senderMoneyAccountId, Long receiverMoneyAccountNumber,
                                         Double payedSum, String assignment) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.receiverMoneyAccountNumber = receiverMoneyAccountNumber;
        this.payedSum = payedSum;
        this.assignment = assignment;
    }

    public MoneyAccPaymentPreparationDto() {
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
