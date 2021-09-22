package org.kosiuk.webApp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CreditCardId implements Serializable {

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "money_account_id")
    private Integer moneyAccountId;

    @Column(name = "id")
    private Integer creditCardId;

    public CreditCardId(Integer accountId, Integer moneyAccountId, Integer creditCardId) {
        this.accountId = accountId;
        this.moneyAccountId = moneyAccountId;
        this.creditCardId = creditCardId;
    }

    public CreditCardId() {

    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(Integer creditCardId) {
        this.creditCardId = creditCardId;
    }

    public Integer getMoneyAccountId() {
        return moneyAccountId;
    }

    public void setMoneyAccountId(Integer moneyAccountId) {
        this.moneyAccountId = moneyAccountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, moneyAccountId, creditCardId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        CreditCardId guest = (CreditCardId) obj;
        return accountId.equals(guest.getAccountId()) && creditCardId.equals(guest.getCreditCardId()) &&
                moneyAccountId.equals(guest.getMoneyAccountId());
    }
}
