package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class PaymentDto {

    private Long number;
    private boolean sent;
    private boolean prepared;
    private Double payedSum;
    private Double comission;
    private String assignment;
    private String timeString;

    public PaymentDto(Long number, boolean sent, boolean prepared, Double payedSum,
                      Double comission, String assignment, String timeString) {
        this.number = number;
        this.sent = sent;
        this.prepared = prepared;
        this.payedSum = payedSum;
        this.comission = comission;
        this.assignment = assignment;
        this.timeString = timeString;
    }

    public PaymentDto() {
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
}
