package org.kosiuk.webApp.paymentSendingObserver;


import org.kosiuk.webApp.service.PaymentService;

@Deprecated
public class PaymentSendingDto implements PaymentSender {

    private Long paymentNumber;
    private Integer senderAccountId;
    private Integer receiverAccountId;
    private PaymentSendingManager paymentSendingManager;
    private PaymentService paymentService;

    public PaymentSendingDto(Long paymentNumber, Integer senderAccountId,
                             Integer receiverAccountId, PaymentSendingManager paymentSendingManager,
                             PaymentService paymentService) {
        this.paymentNumber = paymentNumber;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.paymentSendingManager = paymentSendingManager;
        this.paymentService = paymentService;
    }

    public PaymentSendingDto() {
    }

    @Override
    public boolean send() {
        if (paymentSendingManager.moneyAccountIsFree(senderAccountId) == null &&
                paymentSendingManager.moneyAccountIsFree(receiverAccountId) == null) {

            confirmSending();
            return true;
        } else if (paymentSendingManager.moneyAccountIsFree(senderAccountId) == null) {
            if (paymentSendingManager.moneyAccountIsFree(receiverAccountId)) {

                confirmSending();
                return true;
            } else {
                return false;
            }
        } else if (paymentSendingManager.moneyAccountIsFree(receiverAccountId) == null) {
            if (paymentSendingManager.moneyAccountIsFree(senderAccountId)) {
                confirmSending();
                return true;
            } else {
                return false;
            }
        } else if (paymentSendingManager.moneyAccountIsFree(senderAccountId) &&
        paymentSendingManager.moneyAccountIsFree(receiverAccountId)) {
            confirmSending();
            return true;
        } else {
            return false;
        }
    }

    public void confirmSending() {
        paymentSendingManager.lockMoneyAccount(senderAccountId);
        paymentSendingManager.lockMoneyAccount(receiverAccountId);

        //paymentService.sendPayment(this);

        paymentSendingManager.unsubscribe(this, senderAccountId);
        paymentSendingManager.unsubscribe(this, receiverAccountId);

        paymentSendingManager.unlockMoneyAccount(senderAccountId);
        paymentSendingManager.unlockMoneyAccount(receiverAccountId);

        paymentSendingManager.publishMoneyAccountIsFree(senderAccountId);
        paymentSendingManager.publishMoneyAccountIsFree(receiverAccountId);
    }

    public Long getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Long paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public Integer getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Integer receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public Integer getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Integer senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public PaymentSendingManager getPaymentSendingManager() {
        return paymentSendingManager;
    }

    public void setPaymentSendingManager(PaymentSendingManager paymentSendingManager) {
        this.paymentSendingManager = paymentSendingManager;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
