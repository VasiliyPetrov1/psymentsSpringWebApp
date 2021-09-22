package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.entity.OrderStatus;
import org.kosiuk.webApp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CreditCardOrderWithUserDto {

    private Integer id;
    private OrderStatus orderStatus;
    private String message;
    private boolean visa;
    private boolean masterCard;
    private Integer ownerId;
    private String ownerName;

    public CreditCardOrderWithUserDto(Integer id, OrderStatus orderStatus, String message,
                                      boolean visa, boolean masterCard, Integer ownerId, String ownerName) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.message = message;
        this.visa = visa;
        this.masterCard = masterCard;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    public CreditCardOrderWithUserDto() {

    }

    public CreditCardOrderWithUserDto(Integer id, CreditCardOrderDto creditCardOrderDto, User user) {
        this.id = id;
        this.orderStatus = creditCardOrderDto.getOrderStatus();
        this.message = creditCardOrderDto.getMessage();;
        this.visa = creditCardOrderDto.isVisa();
        this.masterCard = creditCardOrderDto.isMasterCard();
        this.ownerId = user.getId();
        this.ownerName = user.getUsername();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
