package org.kosiuk.webApp.entity;

import org.kosiuk.webApp.util.sumConversion.MoneyIntDecOperator;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "credit_card", uniqueConstraints = {@UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "number")})
public class CreditCard implements MoneyIntDecOperator {

    @EmbeddedId
    private CreditCardId creditCardId;

    @Column(length = 16, nullable = false)
    @NonNull
    private Long number;

    @Column(name = "sum_available_int", scale = 2, nullable = false)
    @NonNull
    private Long sumAvailableInt;

    @Column(name = "sum_available_dec", scale = 2, nullable = false)
    @NonNull
    private Integer sumAvailableDec;

    @Column(length = 3, nullable = false)
    @NonNull
    private Integer cvv;

    @Column(name = "expire_date", nullable = false)
    @NonNull
    private LocalDate expireDate;

    @Column(name = "payment_system", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private PaymentSystem paymentSystem;

    @ManyToOne(cascade = {
            CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH
    })
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @MapsId("accountId")
    private User user;

    @OneToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinColumn(name = "money_account_id",referencedColumnName = "id")
    @MapsId("money_account_id")
    private MoneyAccount moneyAccount;

    @OneToOne(mappedBy = "creditCard", cascade = CascadeType.ALL)
    private CreditCardOrder creditCardOrder;

    public CreditCard(CreditCardId creditCardId, @NonNull Long number, @NonNull Long sumAvailableInt,
                      @NonNull Integer sumAvailableDec, @NonNull Integer cvv, @NonNull LocalDate expireDate,
                      @NonNull PaymentSystem paymentSystem, User user, MoneyAccount moneyAccount,
                      CreditCardOrder creditCardOrder) {
        this.creditCardId = creditCardId;
        this.number = number;
        this.sumAvailableInt = sumAvailableInt;
        this.sumAvailableDec = sumAvailableDec;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.paymentSystem = paymentSystem;
        this.user = user;
        this.moneyAccount = moneyAccount;
        this.creditCardOrder = creditCardOrder;
    }

    public CreditCard() {

    }

    public CreditCardId getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(CreditCardId creditCardId) {
        this.creditCardId = creditCardId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @NonNull
    public Long getSumAvailableInt() {
        return sumAvailableInt;
    }

    public void setSumAvailableInt(@NonNull Long sumAvailableInt) {
        this.sumAvailableInt = sumAvailableInt;
    }

    @NonNull
    public Integer getSumAvailableDec() {
        return sumAvailableDec;
    }

    public void setSumAvailableDec(@NonNull Integer sumAvailableDec) {
        this.sumAvailableDec = sumAvailableDec;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public PaymentSystem getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(PaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MoneyAccount getMoneyAccount() {
        return moneyAccount;
    }

    public void setMoneyAccount(MoneyAccount moneyAccount) {
        this.moneyAccount = moneyAccount;
    }

    public CreditCardOrder getCreditCardOrder() {
        return creditCardOrder;
    }

    public void setCreditCardOrder(CreditCardOrder creditCardOrder) {
        this.creditCardOrder = creditCardOrder;
    }

    @Override
    public long getOperatedSumInt() {
        return getSumAvailableInt();
    }

    @Override
    public int getOperatedSumDec() {
        return getSumAvailableDec();
    }

    public static CreditCard.Builder builder() {
        return new CreditCard.Builder();
    }

    public static class Builder {

        private CreditCardId creditCardId;
        private Long number;
        private Long sumAvailableInt;
        private Integer sumAvailableDec;
        private Integer cvv;
        private LocalDate expireDate;
        private PaymentSystem paymentSystem;
        private User user;
        private MoneyAccount moneyAccount;
        private CreditCardOrder creditCardOrder;

        public CreditCard.Builder id(CreditCardId creditCardId) {
            this.creditCardId = creditCardId;
            return this;
        }

        public CreditCard.Builder number(Long number) {
            this.number = number;
            return this;
        }

        public CreditCard.Builder sumAvailableInt(Long sumAvailableInt) {
            this.sumAvailableInt = sumAvailableInt;
            return this;
        }

        public CreditCard.Builder sumAvailableDec(Integer sumAvailableDec) {
            this.sumAvailableDec = sumAvailableDec;
            return this;
        }

        public CreditCard.Builder cvv(Integer cvv) {
            this.cvv = cvv;
            return this;
        }

        public CreditCard.Builder expireDate(LocalDate expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public CreditCard.Builder paymentSystem(PaymentSystem paymentSystem) {
            this.paymentSystem = paymentSystem;
            return this;
        }

        public CreditCard.Builder user(User user) {
            this.user = user;
            return this;
        }

        public CreditCard.Builder moneyAccount(MoneyAccount moneyAccount){
            this.moneyAccount = moneyAccount;
            return this;
        }

        public CreditCard.Builder creditCardOrder(CreditCardOrder order){
            this.creditCardOrder = order;
            return this;
        }

        public CreditCard build() {
            CreditCard creditCard = new CreditCard();
            creditCard.setCreditCardId(creditCardId);
            creditCard.setNumber(number);
            creditCard.setSumAvailableInt(sumAvailableInt);
            creditCard.setSumAvailableDec(sumAvailableDec);
            creditCard.setCvv(cvv);
            creditCard.setExpireDate(expireDate);
            creditCard.setPaymentSystem(paymentSystem);
            creditCard.setUser(user);
            creditCard.setMoneyAccount(moneyAccount);
            creditCard.setCreditCardOrder(creditCardOrder);
            return creditCard;
        }

    }
}
