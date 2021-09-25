package org.kosiuk.webApp.paymentSendingObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class PaymentSendingManager {

    private final Map<Integer, Boolean> freeMoneyAccounts;
    private final Map<Integer, List<PaymentSender>> paymentsToBeSendByRelAccounts;
    private static PaymentSendingManager instance;

    private PaymentSendingManager(HashMap<Integer, Boolean> freeMoneyAccounts, HashMap<Integer,
            List<PaymentSender>> paymentsToBeSendByRelAccounts) {
        this.freeMoneyAccounts = freeMoneyAccounts;
        this.paymentsToBeSendByRelAccounts = paymentsToBeSendByRelAccounts;
    }

    public static synchronized PaymentSendingManager getInstance() {
        if (instance == null) {
            instance = new PaymentSendingManager(new HashMap<>(), new HashMap<>());
        }
        return instance;
    }

    public void subscribe(PaymentSender paymentSender, Integer moneyAccountId) {
        List<PaymentSender> paymentsToBeSend = paymentsToBeSendByRelAccounts.get(moneyAccountId);
        if (paymentsToBeSend == null) {
            paymentsToBeSend = new ArrayList<>();
            paymentsToBeSendByRelAccounts.put(moneyAccountId, paymentsToBeSend); // put empty for safe synchronizing on it
        }
        synchronized (paymentsToBeSendByRelAccounts.get(moneyAccountId)) {
            paymentsToBeSend.add(paymentSender);
            paymentsToBeSendByRelAccounts.put(moneyAccountId, paymentsToBeSend);
        }
    }

    public void unsubscribe(PaymentSender paymentSender, Integer moneyAccountId) {
        List<PaymentSender> paymentsToBeSend = paymentsToBeSendByRelAccounts.get(moneyAccountId);
        if (paymentsToBeSend != null) {
            synchronized (paymentsToBeSendByRelAccounts.get(moneyAccountId)) {
                paymentsToBeSend.remove(paymentSender);
                paymentsToBeSendByRelAccounts.put(moneyAccountId, paymentsToBeSend);
            }
        }
    }

    public void publishMoneyAccountIsFree(Integer moneyAccountId) {
        List<PaymentSender> paymentsToBeSend = paymentsToBeSendByRelAccounts.get(moneyAccountId);
        synchronized (paymentsToBeSendByRelAccounts.get(moneyAccountId)) {
            for (PaymentSender curSender : paymentsToBeSend) {
                if (curSender.send()) {
                    break;
                }
            }
        }
    }

    public Boolean moneyAccountIsFree(Integer moneyAccountId) {
        return freeMoneyAccounts.get(moneyAccountId);
    }

    public void lockMoneyAccount(Integer moneyAccountId) {
        //Boolean previousValue = freeMoneyAccounts.get(moneyAccountId);
        freeMoneyAccounts.put(moneyAccountId, false);
    }

    public void unlockMoneyAccount(Integer moneyAccountId) {
        freeMoneyAccounts.put(moneyAccountId, true);
    }

    public Map<Integer, Boolean> getFreeMoneyAccounts() {
        return freeMoneyAccounts;
    }

}
