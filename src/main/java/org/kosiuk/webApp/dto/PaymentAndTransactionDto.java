package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class PaymentAndTransactionDto {

    Integer senderAccountId;
    Integer receiverAccountId;
    Double payedSum;
    Double comission;
    String assignment;

    public PaymentAndTransactionDto(Integer senderAccountId, Integer receiverAccountId,
                                    Double payedSum, Double comission, String assignment) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.payedSum = payedSum;
        this.comission = comission;
        this.assignment = assignment;
    }

    public PaymentAndTransactionDto() {

    }

    public Integer getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Integer senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Integer getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Integer receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public Double getPayedSum() {
        return payedSum;
    }

    public void setPayedSum(Double payedSum) {
        this.payedSum = payedSum;
    }

    public Double getComission() {
        return comission;
    }

    public void setComission(Double comission) {
        this.comission = comission;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }
}
