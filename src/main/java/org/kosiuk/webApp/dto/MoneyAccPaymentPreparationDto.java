package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.sumConversion.MoneyStringOperator;
import org.kosiuk.webApp.util.visitor.Visitor;
import org.kosiuk.webApp.util.visitor.VisitorAcceptor;
import org.springframework.stereotype.Component;

@Component
public class MoneyAccPaymentPreparationDto implements MoneyStringOperator, VisitorAcceptor {

    private Integer senderMoneyAccountId;
    private Long receiverMoneyAccountNumber;
    private String payedSumString;
    private String assignment;

    public MoneyAccPaymentPreparationDto(Integer senderMoneyAccountId, Long receiverMoneyAccountNumber,
                                         String payedSumString, String assignment) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.receiverMoneyAccountNumber = receiverMoneyAccountNumber;
        this.payedSumString = payedSumString;
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
        return visitor.visitMoneyAccPaymentPreparationDto(this);
    }
}
