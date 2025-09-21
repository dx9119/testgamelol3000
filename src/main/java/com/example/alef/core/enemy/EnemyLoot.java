package com.example.alef.core.enemy;

import com.example.alef.core.object.Item;
import jakarta.persistence.*;

@Entity
public class EnemyLoot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enemy_id")
    private Enemy enemy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer dropChance; // 0–100
    private Integer quantity;

    public Long getId() { return id; }

    public Enemy getEnemy() { return enemy; }
    public void setEnemy(Enemy enemy) { this.enemy = enemy; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Integer getDropChance() { return dropChance; }
    public void setDropChance(Integer dropChance) { this.dropChance = dropChance; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}

