package com.example.alef.core.market;

import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link Market}
 */
public class MarketDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Set<Long> weapons;

    public MarketDto(Long id, String name, String description, Set<Long> weapons) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weapons = weapons;
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

    public Set<Long> getWeapons() {
        return weapons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketDto entity = (MarketDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.weapons, entity.weapons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, weapons);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "weapons = " + weapons + ")";
    }
}