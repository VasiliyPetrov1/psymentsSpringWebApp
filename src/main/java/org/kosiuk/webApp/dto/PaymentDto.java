package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.sumConversion.MoneyStringOperator;
import org.springframework.stereotype.Component;

@Component
public class PaymentDto implements MoneyStringOperator {

    private Long number;
    private boolean sent;
    private boolean prepared;
    private String payedSumString;
    private String comissionString;
    private String assignment;
    private String timeString;

    public PaymentDto(Long number, boolean sent, boolean prepared, String payedSumString,
                      String comissionString, String assignment, String timeString) {
        this.number = number;
        this.sent = sent;
        this.prepared = prepared;
        this.payedSumString = payedSumString;
        this.comissionString = comissionString;
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

    @Override
    public String getOperatedSumString() {
        return getPayedSumString();
    }

    @Override
    public String getOperatedComissionString() {
        return getComissionString();
    }
}
