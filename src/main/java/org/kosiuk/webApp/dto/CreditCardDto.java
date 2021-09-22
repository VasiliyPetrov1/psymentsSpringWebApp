package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CreditCardDto {

    private Integer id;
    private Long number;
    private Double sumAvailable;
    private Integer cvv;
    private LocalDate expireDate;
    private boolean isVisa;
    private boolean isMasterCard;
    private Integer moneyAccountId;

    public CreditCardDto(Integer id, Long number, Double sumAvailable, Integer cvv, LocalDate expireDate,
                         boolean isVisa, boolean isMasterCard, Integer moneyAccountId) {
        this.id = id;
        this.number = number;
        this.sumAvailable = sumAvailable;
        this.cvv = cvv;
        this.expireDate = expireDate;
        this.isVisa = isVisa;
        this.isMasterCard = isMasterCard;
        this.moneyAccountId = moneyAccountId;
    }

    public CreditCardDto() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Double getSumAvailable() {
        return sumAvailable;
    }

    public void setSumAvailable(Double sumAvailable) {
        this.sumAvailable = sumAvailable;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isVisa() {
        return isVisa;
    }

    public void setVisa(boolean visa) {
        isVisa = visa;
    }

    public boolean isMasterCard() {
        return isMasterCard;
    }

    public void setMasterCard(boolean masterCard) {
        isMasterCard = masterCard;
    }

    public Integer getMoneyAccountId() {
        return moneyAccountId;
    }

    public void setMoneyAccountId(Integer moneyAccountId) {
        this.moneyAccountId = moneyAccountId;
    }
}
