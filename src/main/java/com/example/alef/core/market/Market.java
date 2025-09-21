package com.example.alef.core.market;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Version
    private Integer version;

    private String name;
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "Player_weapons",
            joinColumns = @JoinColumn(name = "weapon_ip")
    )
    @Column(name = "weapon_id")
    private Set<Long> weapons = new HashSet<>();

    public String getName() {
        return name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public Set<Long> getWeapons() {
        return weapons;
    }

    public void setWeapons(Set<Long> weapons) {
        this.weapons = weapons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
