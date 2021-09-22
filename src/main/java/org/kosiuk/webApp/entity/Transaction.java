package org.kosiuk.webApp.entity;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {

    @EmbeddedId
    private TransactionId transactionId;

    @Column(name = "moved_sum", nullable = false, scale = 2)
    private Double movedSum;

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

    public Transaction(TransactionId transactionId, Double movedSum) {
        this.transactionId = transactionId;
        this.movedSum = movedSum;
    }

    public Transaction() {
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public Double getMovedSum() {
        return movedSum;
    }

    public void setMovedSum(Double movedSum) {
        this.movedSum = movedSum;
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
}
