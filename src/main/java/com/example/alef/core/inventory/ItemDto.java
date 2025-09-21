package com.example.alef.core.inventory;

import java.util.Objects;

/**
 * DTO for {@link com.example.alef.core.object.Item}
 */
public class ItemDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Integer rarity;
    private final Integer cost;

    public ItemDto(Long id, String name, String description, Integer rarity, Integer cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRarity() {
        return rarity;
    }

    public Integer getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto entity = (ItemDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.rarity, entity.rarity) &&
                Objects.equals(this.cost, entity.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, rarity, cost);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "rarity = " + rarity + ", " +
                "cost = " + cost + ")";
    }
}