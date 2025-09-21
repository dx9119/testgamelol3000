package com.example.alef.core.inventory;

public class SellDTO {
    private Integer finalCost;

    public SellDTO(Integer finalCost) {
        this.finalCost = finalCost;
    }

    public Integer getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(Integer finalCost) {
        this.finalCost = finalCost;
    }

    @Override
    public String toString() {
        return "SellDTO{" +
                "finalCost=" + finalCost +
                '}';
    }
}
