package org.kosiuk.webApp.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CreditCardOrderId implements Serializable {

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "id")
    private Integer creditCardOrderId;

    public CreditCardOrderId(Integer accountId, Integer creditCardOrderId) {
        this.accountId = accountId;
        this.creditCardOrderId = creditCardOrderId;
    }

    public CreditCardOrderId(Integer accountId) {
        this.accountId = accountId;
    }

    public CreditCardOrderId() {

    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCreditCardOrderId() {
        return creditCardOrderId;
    }

    public void setCreditCardOrderId(Integer creditCardOrderId) {
        this.creditCardOrderId = creditCardOrderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, creditCardOrderId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        CreditCardOrderId guest = (CreditCardOrderId) obj;

        return accountId.equals(guest.getAccountId()) && creditCardOrderId.equals(guest.getCreditCardOrderId());
    }
}
