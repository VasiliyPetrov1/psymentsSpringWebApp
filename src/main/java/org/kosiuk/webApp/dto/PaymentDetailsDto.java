package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class PaymentDetailsDto {
    private Long number;
    private boolean sent;
    private boolean prepared;
    private String payedSumString;
    private String comissionString;
    private String movedSumString;
    private String assignment;
    private String timeString;
    private String senderAccountName;
    private String receiverAccountName;

    public PaymentDetailsDto(Long number, boolean sent, boolean prepared, String payedSumString, String comissionString,
                             String movedSumString, String assignment, String timeString, String senderAccountName,
                             String receiverAccountName) {
        this.number = number;
        this.sent = sent;
        this.prepared = prepared;
        this.payedSumString = payedSumString;
        this.comissionString = comissionString;
        this.movedSumString = movedSumString;
        this.assignment = assignment;
        this.timeString = timeString;
        this.senderAccountName = senderAccountName;
        this.receiverAccountName = receiverAccountName;
    }

    public PaymentDetailsDto() {
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public String getPayedSumString() {
        return payedSumString;
    }

    public void setPayedSumString(String payedSumString) {
        this.payedSumString = payedSumString;
    }

    public String getComissionString() {
        return comissionString;
    }

    public void setComissionString(String comissionString) {
        this.comissionString = comissionString;
    }

    public String getMovedSumString() {
        return movedSumString;
    }

    public void setMovedSumString(String movedSumString) {
        this.movedSumString = movedSumString;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getSenderAccountName() {
        return senderAccountName;
    }

    public void setSenderAccountName(String senderAccountName) {
        this.senderAccountName = senderAccountName;
    }

    public String getReceiverAccountName() {
        return receiverAccountName;
    }

    public void setReceiverAccountName(String receiverAccountName) {
        this.receiverAccountName = receiverAccountName;
    }
}
