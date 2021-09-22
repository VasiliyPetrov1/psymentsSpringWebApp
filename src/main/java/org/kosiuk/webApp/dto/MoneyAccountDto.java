package org.kosiuk.webApp.dto;

import org.springframework.stereotype.Component;

@Component
public class MoneyAccountDto {

    private Integer id;
    private Long number;
    private String name;
    private Double sum;
    private boolean active;
    private boolean blocked;
    private boolean unlockRequested;
    private boolean canBeLocked;

    public MoneyAccountDto(Integer id, Long number, String name, Double sum,
                           boolean active, boolean blocked, boolean unlockRequested, boolean canBeLocked) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.sum = sum;
        this.active = active;
        this.blocked = blocked;
        this.unlockRequested = unlockRequested;
        this.canBeLocked = canBeLocked;
    }

    public MoneyAccountDto(Integer id, Long number, String name,
                           Double sum, boolean active, boolean blocked, boolean unlockRequested) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.sum = sum;
        this.active = active;
        this.blocked = blocked;
        this.unlockRequested = unlockRequested;
    }

    public MoneyAccountDto() {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isUnlockRequested() {
        return unlockRequested;
    }

    public void setUnlockRequested(boolean unlockRequested) {
        this.unlockRequested = unlockRequested;
    }

    public boolean isCanBeLocked() {
        return canBeLocked;
    }

    public void setCanBeLocked(boolean canBeLocked) {
        this.canBeLocked = canBeLocked;
    }
}
