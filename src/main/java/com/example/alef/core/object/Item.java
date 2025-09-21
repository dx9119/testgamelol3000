package com.example.alef.core.object;

import jakarta.persistence.*;

import java.util.concurrent.ThreadLocalRandom;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String description;
    private Integer rarity; // 1–100, влияет на шанс выпадения
    private Integer cost;

    @PrePersist
    public void assignCostIfMissing() {
        System.out.println("⚙️ assignCostIfMissing triggered for item: " + name);
        if (this.cost == null && this.rarity != null) {
            this.cost = generateCostByRarity(this.rarity);
        }
    }

    private int generateCostByRarity(int rarity) {
        int base = rarity * 2;
        return ThreadLocalRandom.current().nextInt(base, base + 50);
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }
}
