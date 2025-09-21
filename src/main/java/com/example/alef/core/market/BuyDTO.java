package com.example.alef.core.market;

public class BuyDTO {
    private Integer sellCost;
    private Long idWeapon;
    private String status;

    public Integer getSellCost() {
        return sellCost;
    }

    public void setSellCost(Integer sellCost) {
        this.sellCost = sellCost;
    }

    public Long getIdWeapon() {
        return idWeapon;
    }

    public void setIdWeapon(Long idWeapon) {
        this.idWeapon = idWeapon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
