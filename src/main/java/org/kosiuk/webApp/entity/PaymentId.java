package org.kosiuk.webApp.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PaymentId implements Serializable {

    @Column(name = "sender_money_account_id")
    private Integer senderMoneyAccountId;

    @Column(name = "number_")
    private Long number;

    public PaymentId(Integer senderMoneyAccountId, Long number) {
        this.senderMoneyAccountId = senderMoneyAccountId;
        this.number = number;
    }

    public PaymentId() {
    }

    public Integer getSenderMoneyAccountId() {
        return senderMoneyAccountId;
    }

    public void setSenderMoneyAccountId(Integer senderMoneyAccountId) {
        this.senderMoneyAccountId = senderMoneyAccountId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()){
           return false;
        }

        PaymentId guest = (PaymentId) obj;

        return senderMoneyAccountId.equals(guest.getSenderMoneyAccountId()) &&
                number.equals(guest.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderMoneyAccountId, number);
    }
}
