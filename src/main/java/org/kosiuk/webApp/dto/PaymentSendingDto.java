package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.sumConversion.MoneyIntDecOperator;
import org.springframework.stereotype.Component;

@Component
public class PaymentSendingDto implements MoneyIntDecOperator {

    private Integer senderAccountId;
    private Integer receiverAccountId;
    private Long paymentNumber;
    private Long payedSumInt;
    private Integer payedSumDec;
    private Long comissionInt;
    private Integer comissionDec;
    String assignment;

    public PaymentSendingDto(Integer senderAccountId, Integer receiverAccountId, Long paymentNumber,
                             Long payedSumInt, Integer payedSumDec, Long comissionInt,
                             Integer comissionDec, String assignment) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.paymentNumber = paymentNumber;
        this.payedSumInt = payedSumInt;
        this.payedSumDec = payedSumDec;
        this.comissionInt = comissionInt;
        this.comissionDec = comissionDec;
        this.assignment = assignment;
    }

    public PaymentSendingDto() {

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

    public Long getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Long paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    @Override
    public long getOperatedSumInt() {
        return getPayedSumInt();
    }

    @Override
    public int getOperatedSumDec() {
        return getPayedSumDec();
    }

    @Override
    public long getOperatedComissionInt() {
        return getComissionInt();
    }

    @Override
    public int getOperatedComissionDec() {
        return getComissionDec();
    }
}
