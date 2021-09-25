package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.sumConversion.MoneyStringOperator;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentConfirmationDto implements MoneyStringOperator {

    private Integer senderMoneyAccountId;
    private Integer receiverMoneyAccountId;
    private Long receiverCreditCardNumber;
    private Long paymentNumber;
    private String receiverAccountName;
    private String payedSumString;
    private String assignment;
    private String movedSumString;
    private String paymentComissionString;

    public CardPaymentConfirmationDto(Integer senderMoneyAccountId, Integer receiverMoneyAccountId, Long receiverCreditCardNumber,
                                      Long paymentNumber, String receiverAccountName, String payedSumString,
                                      String assignment, String movedSumString, String paymentComissionString) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.receiverMoneyAccountId = receiverMoneyAccountId;
        this.receiverCreditCardNumber = receiverCreditCardNumber;
        this.paymentNumber = paymentNumber;
        this.receiverAccountName = receiverAccountName;
        this.payedSumString = payedSumString;
        this.assignment = assignment;
        this.movedSumString = movedSumString;
        this.paymentComissionString = paymentComissionString;
    }

    public CardPaymentConfirmationDto() {
    }

    public Integer getSenderMoneyAccountId() {
        return senderMoneyAccountId;
    }

    public void setSenderMoneyAccountId(Integer senderMoneyAccountId) {
        this.senderMoneyAccountId = senderMoneyAccountId;
    }

    public Integer getReceiverMoneyAccountId() {
        return receiverMoneyAccountId;
    }

    public void setReceiverMoneyAccountId(Integer receiverMoneyAccountId) {
        this.receiverMoneyAccountId = receiverMoneyAccountId;
    }

    public Long getReceiverCreditCardNumber() {
        return receiverCreditCardNumber;
    }

    public void setReceiverCreditCardNumber(Long receiverCreditCardNumber) {
        this.receiverCreditCardNumber = receiverCreditCardNumber;
    }

    public Long getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Long paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getReceiverAccountName() {
        return receiverAccountName;
    }

    public void setReceiverAccountName(String receiverAccountName) {
        this.receiverAccountName = receiverAccountName;
    }

    public String getPayedSumString() {
        return payedSumString;
    }

    public void setPayedSumString(String payedSumString) {
        this.payedSumString = payedSumString;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }



    public String getPaymentComissionString() {
        return paymentComissionString;
    }

    public void setPaymentComissionString(String paymentComissionString) {
        this.paymentComissionString = paymentComissionString;
    }

    public String getMovedSumString() {
        return movedSumString;
    }

    public void setMovedSumString(String movedSumString) {
        this.movedSumString = movedSumString;
    }

    @Override
    public String getOperatedSumString() {
        return getPayedSumString();
    }

    @Override
    public String getOperatedComissionString() {
        return getPaymentComissionString();
    }
}
