package org.kosiuk.webApp.entity;

import org.kosiuk.webApp.util.sumConversion.MoneyIntDecOperator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment implements MoneyIntDecOperator {

    @EmbeddedId
    private PaymentId paymentId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "payed_sum_int",  nullable = false)
    private Long payedSumInt;

    @Column(name = "payed_sum_dec",  nullable = false)
    private Integer payedSumDec;

    @Column(name = "comission_int", nullable = false)
    private Long comissionInt;

    @Column(name = "comission_dec", nullable = false)
    private Integer comissionDec;

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

    public Payment(PaymentId paymentId, PaymentStatus status, Long payedSumInt, Integer payedSumDec, Long comissionInt,
                   Integer comissionDec, String assignment, LocalDateTime time) {
        this.paymentId = paymentId;
        this.status = status;
        this.payedSumInt = payedSumInt;
        this.payedSumDec = payedSumDec;
        this.comissionInt = comissionInt;
        this.comissionDec = comissionDec;
        this.assignment = assignment;
        this.time = time;
    }

    public Payment(PaymentId paymentId, PaymentStatus status, Long payedSumInt, Integer payedSumDec) {
        this.paymentId = paymentId;
        this.status = status;
        this.payedSumInt = payedSumInt;
        this.payedSumDec = payedSumDec;
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

    public Long getPayedSumInt() {
        return payedSumInt;
    }

    public void setPayedSumInt(Long payedSumInt) {
        this.payedSumInt = payedSumInt;
    }

    public Integer getPayedSumDec() {
        return payedSumDec;
    }

    public void setPayedSumDec(Integer payedSumDec) {
        this.payedSumDec = payedSumDec;
    }

    public Long getComissionInt() {
        return comissionInt;
    }

    public void setComissionInt(Long comissionInt) {
        this.comissionInt = comissionInt;
    }

    public Integer getComissionDec() {
        return comissionDec;
    }

    public void setComissionDec(Integer comissionDec) {
        this.comissionDec = comissionDec;
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

    @Override
    public long getOperatedSumInt() {
        return getPayedSumInt();
    }

    @Override
    public int getOperatedSumDec() {
        return getPayedSumDec();
    }

    @Override
    public long getOperatedComissionInt() {
        return getComissionInt();
    }

    @Override
    public int getOperatedComissionDec() {
        return getComissionDec();
    }
}
