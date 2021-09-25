package org.kosiuk.webApp.entity;

import org.kosiuk.webApp.util.sumConversion.MoneyIntDecOperator;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction implements MoneyIntDecOperator {

    @EmbeddedId
    private TransactionId transactionId;

    @Column(name = "moved_sum_int", nullable = false)
    @NonNull
    private Long movedSumInt;

    @Column(name = "moved_sum_dec", nullable = false)
    @NonNull
    private Integer movedSumDec;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "payment_number", referencedColumnName = "number_", insertable = false, updatable = false),
            @JoinColumn(name = "sender_money_account_id", referencedColumnName = "sender_money_account_id",
                    insertable = false, updatable = false)
    })
    private Payment payment;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinColumn(name = "receiver_money_account_id", referencedColumnName = "id")
    @MapsId("receiverMoneyAccountId")
    private MoneyAccount receiverMoneyAccount;

    public Transaction(TransactionId transactionId, @NonNull Long movedSumInt, @NonNull Integer movedSumDec,
                       Payment payment, MoneyAccount receiverMoneyAccount) {
        this.transactionId = transactionId;
        this.movedSumInt = movedSumInt;
        this.movedSumDec = movedSumDec;
        this.payment = payment;
        this.receiverMoneyAccount = receiverMoneyAccount;
    }

    public Transaction(TransactionId transactionId, @NonNull Long movedSumInt, @NonNull Integer movedSumDec) {
        this.transactionId = transactionId;
        this.movedSumInt = movedSumInt;
        this.movedSumDec = movedSumDec;
    }

    public Transaction() {
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    @NonNull
    public Long getMovedSumInt() {
        return movedSumInt;
    }

    public void setMovedSumInt(@NonNull Long movedSumInt) {
        this.movedSumInt = movedSumInt;
    }

    @NonNull
    public Integer getMovedSumDec() {
        return movedSumDec;
    }

    public void setMovedSumDec(@NonNull Integer movedSumDec) {
        this.movedSumDec = movedSumDec;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public MoneyAccount getReceiverMoneyAccount() {
        return receiverMoneyAccount;
    }

    public void setReceiverMoneyAccount(MoneyAccount receiverMoneyAccount) {
        this.receiverMoneyAccount = receiverMoneyAccount;
    }

    @Override
    public long getOperatedSumInt() {
        return getMovedSumInt();
    }

    @Override
    public int getOperatedSumDec() {
        return getMovedSumDec();
    }
}
