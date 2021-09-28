package org.kosiuk.webApp.entity;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "credit_card_order")
public class CreditCardOrder {

    @EmbeddedId
    private CreditCardOrderId creditCardOrderId;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private OrderStatus orderStatus;

    @Column(name = "message", length = 200)
    private String message;

    @Column(name = "desired_payment_system", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private PaymentSystem desPaymentSystem;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @MapsId("accountId")
    private User user;

    @OneToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinColumns({
            @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false),
            @JoinColumn(name = "credit_card_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "money_account_id", referencedColumnName = "money_account_id", insertable = false, updatable = false)
    })
    private CreditCard creditCard;

    public CreditCardOrder(CreditCardOrderId creditCardOrderId, @NonNull OrderStatus orderStatus, String message,
                           @NonNull PaymentSystem desPaymentSystem, User user, CreditCard creditCard) {
        this.creditCardOrderId = creditCardOrderId;
        this.orderStatus = orderStatus;
        this.message = message;
        this.desPaymentSystem = desPaymentSystem;
        this.user = user;
        this.creditCard = creditCard;
    }

    public CreditCardOrder() {

    }

    public CreditCardOrderId getCreditCardOrderId() {
        return creditCardOrderId;
    }

    public void setCreditCardOrderId(CreditCardOrderId creditCardOrderId) {
        this.creditCardOrderId = creditCardOrderId;
    }

    @NonNull
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(@NonNull OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    public PaymentSystem getDesPaymentSystem() {
        return desPaymentSystem;
    }

    public void setDesPaymentSystem(@NonNull PaymentSystem desPaymentSystem) {
        this.desPaymentSystem = desPaymentSystem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public static CreditCardOrder.Builder builder() {
        return new CreditCardOrder.Builder();
    }

    public static class Builder {

        private CreditCardOrderId creditCardOrderId;
        private OrderStatus orderStatus;
        private String message;
        private PaymentSystem desPaymentSystem;
        private User user;
        private CreditCard creditCard;

        public CreditCardOrder.Builder id(CreditCardOrderId creditCardOrderId) {
            this.creditCardOrderId = creditCardOrderId;
            return this;
        }

        public CreditCardOrder.Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public CreditCardOrder.Builder message(String message) {
            this.message = message;
            return this;
        }

        public CreditCardOrder.Builder desPaymentSystem(PaymentSystem desPaymentSystem) {
            this.desPaymentSystem = desPaymentSystem;
            return this;
        }

        public CreditCardOrder.Builder user(User user) {
            this.user = user;
            return this;
        }

        public CreditCardOrder.Builder creditCard(CreditCard creditCard){
            this.creditCard = creditCard;
            return this;
        }

        public CreditCardOrder build() {
            CreditCardOrder order = new CreditCardOrder();
            order.setCreditCardOrderId(creditCardOrderId);
            order.setOrderStatus(orderStatus);
            order.setMessage(message);
            order.setDesPaymentSystem(desPaymentSystem);
            order.setUser(user);
            order.setCreditCard(creditCard);
            return order;
        }

    }
}
