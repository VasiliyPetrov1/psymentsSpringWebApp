package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CreditCardConfirmationDto {

    private Long number;
    private Integer cvv;
    private String expireDateString;
    boolean visa;
    boolean masterCard;

    public CreditCardConfirmationDto(Long number, Integer cvv, String expireDateString, boolean visa, boolean masterCard) {
        this.number = number;
        this.cvv = cvv;
        this.expireDateString = expireDateString;
        this.visa = visa;
        this.masterCard = masterCard;
    }

    public CreditCardConfirmationDto() {

    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public String getExpireDateString() {
        return expireDateString;
    }

    public void setExpireDateString(String expireDateString) {
        this.expireDateString = expireDateString;
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
}
