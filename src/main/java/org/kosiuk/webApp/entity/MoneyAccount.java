package org.kosiuk.webApp.entity;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "money_account", uniqueConstraints = {@UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "number")})
public class MoneyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(length = 12, nullable = false)
    @NonNull
    private Long number;

    @Column(length = 45, nullable = false)
    private String name;

    @NonNull
    @Column(scale = 2, nullable = false)
    private Double sum;

    @Column(name = "active", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private MoneyAccountActStatus active;

    @OneToOne(mappedBy = "moneyAccount", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private CreditCard creditCard;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_money_account_id")
    private List<Payment> payments;

    @OneToMany(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinColumn(name = "receiver_money_account_id")
    private List<Transaction> receivedTransactions;

    public MoneyAccount(Integer id, @NonNull Long number, String name, @NonNull Double sum,
                        @NonNull MoneyAccountActStatus active) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.sum = sum;
        this.active = active;
    }

    public MoneyAccount(@NonNull Long number, String name, @NonNull Double sum, @NonNull MoneyAccountActStatus active) {
        this.number = number;
        this.name = name;
        this.sum = sum;
        this.active = active;
    }

    public MoneyAccount() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    @NonNull
    public MoneyAccountActStatus getActive() {
        return active;
    }

    public void setActive(@NonNull MoneyAccountActStatus active) {
        this.active = active;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(List<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }
}
