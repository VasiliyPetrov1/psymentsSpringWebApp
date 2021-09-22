package org.kosiuk.webApp.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TransactionId implements Serializable {

    @Column(name = "payment_number")
    private Long paymentNumber;

    @Column(name = "sender_money_account_id")
    private Integer senderMoneyAccountId;

    @Column(name = "receiver_money_account_id")
    private Integer receiverMoneyAccountId;

    public TransactionId(Long paymentNumber, Integer senderMoneyAccountId, Integer receiverMoneyAccountId) {
        this.paymentNumber = paymentNumber;
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.receiverMoneyAccountId = receiverMoneyAccountId;
    }

    public TransactionId() {
    }

    public Long getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Long paymentNumber) {
        this.paymentNumber = paymentNumber;
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

    @Override
    public int hashCode() {
        return Objects.hash(paymentNumber, senderMoneyAccountId, receiverMoneyAccountId);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        TransactionId guest = (TransactionId) obj;

        return paymentNumber.equals(guest.getPaymentNumber()) && senderMoneyAccountId.equals(guest.getSenderMoneyAccountId()) &&
                receiverMoneyAccountId.equals(guest.getReceiverMoneyAccountId());
    }
}
