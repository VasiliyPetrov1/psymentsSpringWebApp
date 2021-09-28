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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sender_money_account_id", updatable = false)
    private List<Payment> payments;

    @OneToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    }, orphanRemoval = true)
    @JoinColumn(name = "receiver_money_account_id", updatable = false)
    private List<Transaction> receivedTransactions;

    public MoneyAccount(Integer id, @NonNull Long number, String name, @NonNull Long sumInt, @NonNull Integer sumDec,
                        @NonNull Long curSumAvailableInt, @NonNull Integer curSumAvailableDec,
                        @NonNull MoneyAccountActStatus active, CreditCard creditCard, List<Payment> payments,
                        List<Transaction> receivedTransactions) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.sumInt = sumInt;
        this.sumDec = sumDec;
        this.curSumAvailableInt = curSumAvailableInt;
        this.curSumAvailableDec = curSumAvailableDec;
        this.active = active;
        this.creditCard = creditCard;
        this.payments = payments;
        this.receivedTransactions = receivedTransactions;
    }

    /*public MoneyAccount(Integer id, @NonNull Long number, String name, @NonNull Long sumInt, @NonNull Integer sumDec,
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
    }*/

    public MoneyAccount() {

    }

    public static MoneyAccount.Builder builder() {
        return new MoneyAccount.Builder();
    }

    public static class Builder {

        private Integer id;
        private Long number;
        private String name;
        private Long sumInt;
        private Integer sumDec;
        private Long curSumAvailableInt;
        private Integer curSumAvailableDec;
        private MoneyAccountActStatus active;
        private CreditCard creditCard;
        private List<Payment> payments;
        private List<Transaction> receivedTransactions;


        public MoneyAccount.Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public MoneyAccount.Builder number(Long number) {
            this.number = number;
            return this;
        }

        public MoneyAccount.Builder name(String name) {
            this.name = name;
            return this;
        }

        public MoneyAccount.Builder sumInt(Long sumInt) {
            this.sumInt = sumInt;
            return this;
        }

        public MoneyAccount.Builder sumDec(Integer sumDec) {
            this.sumDec = sumDec;
            return this;
        }

        public MoneyAccount.Builder sumAvailableInt(Long curSumAvailableInt) {
            this.curSumAvailableInt = curSumAvailableInt;
            return this;
        }

        public MoneyAccount.Builder sumAvailableDec(Integer curSumAvailableDec) {
            this.curSumAvailableDec = curSumAvailableDec;
            return this;
        }

        public MoneyAccount.Builder active(MoneyAccountActStatus active) {
            this.active = active;
            return this;
        }

        public MoneyAccount.Builder creditCard(CreditCard creditCard) {
            this.creditCard = creditCard;
            return this;
        }

        public MoneyAccount.Builder payments(List<Payment> payments) {
            this.payments = payments;
            return this;
        }

        public MoneyAccount.Builder receivedTransactions(List<Transaction> receivedTransactions) {
            this.receivedTransactions = receivedTransactions;
            return this;
        }

        public MoneyAccount build() {
            MoneyAccount moneyAccount = new MoneyAccount();
            moneyAccount.setId(id);
            moneyAccount.setNumber(number);
            moneyAccount.setName(name);
            moneyAccount.setSumInt(sumInt);
            moneyAccount.setSumDec(sumDec);
            moneyAccount.setCurSumAvailableInt(curSumAvailableInt);
            moneyAccount.setCurSumAvailableDec(curSumAvailableDec);
            moneyAccount.setActive(active);
            moneyAccount.setCreditCard(creditCard);
            moneyAccount.setPayments(payments);
            moneyAccount.setReceivedTransactions(receivedTransactions);
            return moneyAccount;
        }

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
