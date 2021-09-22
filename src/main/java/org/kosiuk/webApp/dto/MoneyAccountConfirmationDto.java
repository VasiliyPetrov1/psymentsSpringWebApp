package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class MoneyAccountConfirmationDto {

    private Long number;
    private String name;

    public MoneyAccountConfirmationDto(Long number, String name) {
        this.number = number;
        this.name = name;
    }

    public MoneyAccountConfirmationDto() {

    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
