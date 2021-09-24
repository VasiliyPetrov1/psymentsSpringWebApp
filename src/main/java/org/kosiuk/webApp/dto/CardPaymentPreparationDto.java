package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.sumConversion.MoneyStringOperator;
import org.kosiuk.webApp.util.visitor.Visitor;
import org.kosiuk.webApp.util.visitor.VisitorAcceptor;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentPreparationDto implements MoneyStringOperator, VisitorAcceptor {

    private Integer senderMoneyAccountId;
    private Long receiverCreditCardNumber;
    private String payedSumString;
    private String assignment;

    public CardPaymentPreparationDto(Integer senderMoneyAccountId, Long receiverCreditCardNumber,
                                     String payedSum, String assignment) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.receiverCreditCardNumber = receiverCreditCardNumber;
        this.payedSumString = payedSum;
        this.assignment = assignment;
    }

    public CardPaymentPreparationDto() {
    }

    public Integer getSenderMoneyAccountId() {
        return senderMoneyAccountId;
    }

    public void setSenderMoneyAccountId(Integer senderMoneyAccountId) {
        this.senderMoneyAccountId = senderMoneyAccountId;
    }

    public Long getReceiverCreditCardNumber() {
        return receiverCreditCardNumber;
    }

    public void setReceiverCreditCardNumber(Long receiverCreditCardNumber) {
        this.receiverCreditCardNumber = receiverCreditCardNumber;
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

    @Override
    public String getOperatedSumString() {
        return getPayedSumString();
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitCardPaymentPreparationDto(this);
    }
}
