package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class PaymentAndTransactionDto {

    Integer senderAccountId;
    Integer receiverAccountId;
    Long payedSumInt;
    Integer payedSumDec;
    Long comissionInt;
    Integer comissionDec;
    String assignment;

    public PaymentAndTransactionDto(Integer senderAccountId, Integer receiverAccountId,
                                    Long payedSumInt, Integer payedSumDec, Long comissionInt,
                                    Integer comissionDec, String assignment) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.payedSumInt = payedSumInt;
        this.payedSumDec = payedSumDec;
        this.comissionInt = comissionInt;
        this.comissionDec = comissionDec;
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

    public Long getPayedSumInt() {
        return payedSumInt;
    }

    public void setPayedSumInt(Long payedSumInt) {
        this.payedSumInt = payedSumInt;
    }

    public Integer getPayedSumDec() {
        return payedSumDec;
    }

    public void setPayedSumDec(Integer payedSumDec) {
        this.payedSumDec = payedSumDec;
    }

    public Long getComissionInt() {
        return comissionInt;
    }

    public void setComissionInt(Long comissionInt) {
        this.comissionInt = comissionInt;
    }

    public Integer getComissionDec() {
        return comissionDec;
    }

    public void setComissionDec(Integer comissionDec) {
        this.comissionDec = comissionDec;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }
}
