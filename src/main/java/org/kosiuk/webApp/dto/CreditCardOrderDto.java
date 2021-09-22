package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.entity.OrderStatus;
import org.kosiuk.webApp.util.visitor.Visitor;
import org.kosiuk.webApp.util.visitor.VisitorAcceptor;
import org.springframework.stereotype.Component;

@Component
public class CreditCardOrderDto implements VisitorAcceptor {


    private OrderStatus orderStatus;
    private String message;
    private boolean visa;
    private boolean masterCard;

    public CreditCardOrderDto(OrderStatus orderStatus, String message, boolean visa, boolean masterCard) {
        this.orderStatus = orderStatus;
        this.message = message;
        this.visa = visa;
        this.masterCard = masterCard;
    }

    public CreditCardOrderDto() {

    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVisa() {
        return visa;
    }

    public void setVisa(boolean visa) {
        this.visa = visa;
    }

    public boolean isMasterCard() {
        return masterCard;
    }

    public void setMasterCard(boolean masterCard) {
        this.masterCard = masterCard;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitCreditCardOrderDto(this);
    }
}
