package org.kosiuk.webApp.entity;

import org.kosiuk.webApp.util.sumConversion.MoneyIntDecOperator;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "money_account", uniqueConstraints = {@UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "number")})
public class MoneyAccount implements MoneyIntDecOperator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(length = 12, nullable = false)
    @NonNull
    private Long number;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(name = "sum_int", nullable = false)
    @NonNull
    private Long sumInt;

    @Column(name = "sum_dec", nullable = false)
    @NonNull
    private Integer sumDec;

    @Column(name = "cur_sum_available_int", nullable = false)
    @NonNull
    private Long curSumAvailableInt;

    @Column(name = "cur_sum_available_dec", nullable = false)
    @NonNull
    private Integer curSumAvailableDec;

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

    public MoneyAccount(Integer id, @NonNull Long number, String name, @NonNull Long sumInt, @NonNull Integer sumDec,
                        @NonNull Long curSumAvailableInt, @NonNull Integer curSumAvailableDec,
                        @NonNull MoneyAccountActStatus active) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.sumInt = sumInt;
        this.sumDec = sumDec;
        this.curSumAvailableInt = curSumAvailableInt;
        this.curSumAvailableDec = curSumAvailableDec;
        this.active = active;
    }

    public MoneyAccount(@NonNull Long number, String name, @NonNull Long sumInt, @NonNull Integer sumDec,
                        @NonNull Long curSumAvailableInt, @NonNull Integer curSumAvailableDec,
                        @NonNull MoneyAccountActStatus active) {
        this.number = number;
        this.name = name;
        this.sumInt = sumInt;
        this.sumDec = sumDec;
        this.curSumAvailableInt = curSumAvailableInt;
        this.curSumAvailableDec = curSumAvailableDec;
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

    @NonNull
    public Long getSumInt() {
        return sumInt;
    }

    public void setSumInt(@NonNull Long sumInt) {
        this.sumInt = sumInt;
    }

    @NonNull
    public Integer getSumDec() {
        return sumDec;
    }

    public void setSumDec(@NonNull Integer sumDec) {
        this.sumDec = sumDec;
    }

    @NonNull
    public Long getCurSumAvailableInt() {
        return curSumAvailableInt;
    }

    public void setCurSumAvailableInt(@NonNull Long curSumAvailableInt) {
        this.curSumAvailableInt = curSumAvailableInt;
    }

    @NonNull
    public Integer getCurSumAvailableDec() {
        return curSumAvailableDec;
    }

    public void setCurSumAvailableDec(@NonNull Integer curSumAvailableDec) {
        this.curSumAvailableDec = curSumAvailableDec;
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

    @Override
    public long getOperatedSumInt() {
        return getSumInt();
    }

    @Override
    public int getOperatedSumDec() {
        return getSumDec();
    }
}
