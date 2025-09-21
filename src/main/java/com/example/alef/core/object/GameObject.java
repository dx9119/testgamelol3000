package com.example.alef.core.object;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GameObject{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long HP = 100L;
    private String name ="Default";
    private Boolean visible = true;
    private Boolean destroy = false;
    private Instant timeOfdestroy;

    @Version
    private Integer version;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "gameobject_loot",
            joinColumns = @JoinColumn(name = "gameobject_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> lootTable = new ArrayList<>();

    public Instant getTimeOfdestroy() {
        return timeOfdestroy;
    }

    public void setTimeOfdestroy(Instant timeOfdestroy) {
        this.timeOfdestroy = timeOfdestroy;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Item> getLootTable() {
        return lootTable;
    }

    public void setLootTable(List<Item> lootTable) {
        this.lootTable = lootTable;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHP() {
        return HP;
    }

    public void setHP(Long HP) {
        this.HP = HP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getDestroy() {
        return destroy;
    }

    public void setDestroy(Boolean destroy) {
        this.destroy = destroy;
    }
}
