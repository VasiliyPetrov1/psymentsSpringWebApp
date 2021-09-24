package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.util.sumConversion.MoneyStringOperator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CreditCardDto implements MoneyStringOperator {

    private Integer id;
    private Long number;
    private String sumAvailableString;
    private Integer cvv;
    private LocalDate expireDate;
    private boolean isVisa;
    private boolean isMasterCard;
    private Integer moneyAccountId;

    public CreditCardDto(Integer id, Long number, String sumAvailableString, Integer cvv, LocalDate expireDate,
                         boolean isVisa, boolean isMasterCard, Integer moneyAccountId) {
        this.id = id;
        this.number = number;
        this.sumAvailableString = sumAvailableString;
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

    public String getSumAvailableString() {
        return sumAvailableString;
    }

    public void setSumAvailableString(String sumAvailableString) {
        this.sumAvailableString = sumAvailableString;
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

    @Override
    public String getOperatedSumString() {
        return getSumAvailableString();
    }
}
