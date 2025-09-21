package com.example.alef.core.player;

import jakarta.persistence.*;

@Entity
public class Weapon {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    private Integer version;


    @OneToOne(mappedBy = "weapon")
    private Player owner;

    private String name = "Test";
    private Integer downDamage=0;
    private Integer upDamage=1;
    private Integer distance = 2;
    private Integer cost;
    private Integer availableEnergy = 15; //общее кол-во энергии
    private Integer energyCostForAttack = 15;

    public Integer getAvailableEnergy() {
        return availableEnergy;
    }

    public void setAvailableEnergy(Integer availableEnergy) {
        this.availableEnergy = availableEnergy;
    }

    public Integer getEnergyCostForAttack() {
        return energyCostForAttack;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setEnergyCostForAttack(Integer energyCostForAttack) {
        this.energyCostForAttack = energyCostForAttack;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Integer getDownDamage() {
        return downDamage;
    }

    public void setDownDamage(Integer downDamage) {
        this.downDamage = downDamage;
    }

    public Integer getUpDamage() {
        return upDamage;
    }

    public void setUpDamage(Integer upDamage) {
        this.upDamage = upDamage;
    }
}
