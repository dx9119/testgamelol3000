package com.example.alef.core.inventory;

import com.example.alef.core.object.Item;
import com.example.alef.core.player.Player;
import jakarta.persistence.*;

@Entity
public class PlayerItem {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Integer version;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Item item;

    private Integer count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

