package org.kosiuk.webApp.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @EmbeddedId
    private PaymentId paymentId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "payed_sum", scale = 2, nullable = false)
    private Double payedSum;

    @Column(name = "comission", scale = 2, nullable = false)
    private Double comission;

    @Column(name = "assignment", length = 200)
    private String assignment;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinColumn(name = "sender_money_account_id", referencedColumnName = "id")
    @MapsId("senderMoneyAccountId")
    private MoneyAccount moneyAccount;

    @OneToOne(mappedBy = "payment", cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    private Transaction transaction;

    public Payment(PaymentId paymentId, PaymentStatus status, Double payedSum, Double comission,
                   String assignment, LocalDateTime time) {
        this.paymentId = paymentId;
        this.status = status;
        this.payedSum = payedSum;
        this.comission = comission;
        this.assignment = assignment;
        this.time = time;
    }

    public Payment(PaymentId paymentId, PaymentStatus status, Double payedSum) {
        this.paymentId = paymentId;
        this.status = status;
        this.payedSum = payedSum;
    }

    public Payment() {
    }

    public PaymentId getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(PaymentId paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Double getPayedSum() {
        return payedSum;
    }

    public void setPayedSum(Double payedSum) {
        this.payedSum = payedSum;
    }

    public Double getComission() {
        return comission;
    }

    public void setComission(Double comission) {
        this.comission = comission;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public MoneyAccount getMoneyAccount() {
        return moneyAccount;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public void setMoneyAccount(MoneyAccount moneyAccount) {
        this.moneyAccount = moneyAccount;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
