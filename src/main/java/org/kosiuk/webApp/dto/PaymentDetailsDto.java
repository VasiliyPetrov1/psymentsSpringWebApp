package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class PaymentDetailsDto {
    private Long number;
    private boolean sent;
    private boolean prepared;
    private Double payedSum;
    private Double comission;
    private Double movedSum;
    private String assignment;
    private String timeString;
    private String senderAccountName;
    private String receiverAccountName;

    public PaymentDetailsDto(Long number, boolean sent, boolean prepared, Double payedSum, Double comission,
                             Double movedSum, String assignment, String timeString, String senderAccountName,
                             String receiverAccountName) {
        this.number = number;
        this.sent = sent;
        this.prepared = prepared;
        this.payedSum = payedSum;
        this.comission = comission;
        this.movedSum = movedSum;
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

    public Double getMovedSum() {
        return movedSum;
    }

    public void setMovedSum(Double movedSum) {
        this.movedSum = movedSum;
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
