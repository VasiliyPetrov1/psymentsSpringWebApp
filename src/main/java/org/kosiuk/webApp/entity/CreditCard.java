package org.kosiuk.webApp.entity;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "credit_card", uniqueConstraints = {@UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "number")})
public class CreditCard {

    @EmbeddedId
    private CreditCardId creditCardId;

    @Column(length = 16, nullable = false)
    @NonNull
    private Long number;

    @Column(name = "sum_available", scale = 2, nullable = false)
    @NonNull
    private Double sumAvailable;

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

    public CreditCard(CreditCardId creditCardId, @NonNull Long number, @NonNull Double sumAvailable,
                      @NonNull Integer cvv, @NonNull LocalDate expireDate, @NonNull PaymentSystem paymentSystem) {
        this.creditCardId = creditCardId;
        this.number = number;
        this.sumAvailable = sumAvailable;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.paymentSystem = paymentSystem;
    }

    public CreditCard(Long number, Double sumAvailable, Integer cvv, LocalDate expireDate,
                      PaymentSystem paymentSystem) {
        this.number = number;
        this.sumAvailable = sumAvailable;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.paymentSystem = paymentSystem;
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

    public Double getSumAvailable() {
        return sumAvailable;
    }

    public void setSumAvailable(Double sumAvailable) {
        this.sumAvailable = sumAvailable;
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
}
