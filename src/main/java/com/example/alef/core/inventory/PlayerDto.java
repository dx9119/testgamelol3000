package com.example.alef.core.inventory;

import java.util.Objects;

/**
 * DTO for {@link com.example.alef.core.player.Player}
 */
public class PlayerDto {
    private final Long id;
    private final String name;
    private final String colorName;
    private final String colorBgName;
    private final String colorBody;
    private final Long HP;
    private final Integer speed;
    private final boolean isMove;
    private final Integer vision;
    private final Integer countDead;
    private final Integer countWin;
    private final Integer bibaCoinsCount;
    private final Long lastMoveTime;

    public PlayerDto(Long id, String name, String colorName, String colorBgName, String colorBody, Long HP, Integer speed, boolean isMove, Integer vision, Integer countDead, Integer countWin, Integer bibaCoinsCount, Long lastMoveTime) {
        this.id = id;
        this.name = name;
        this.colorName = colorName;
        this.colorBgName = colorBgName;
        this.colorBody = colorBody;
        this.HP = HP;
        this.speed = speed;
        this.isMove = isMove;
        this.vision = vision;
        this.countDead = countDead;
        this.countWin = countWin;
        this.bibaCoinsCount = bibaCoinsCount;
        this.lastMoveTime = lastMoveTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorBgName() {
        return colorBgName;
    }

    public String getColorBody() {
        return colorBody;
    }

    public Long getHP() {
        return HP;
    }

    public Integer getSpeed() {
        return speed;
    }

    public boolean getIsMove() {
        return isMove;
    }

    public Integer getVision() {
        return vision;
    }

    public Integer getCountDead() {
        return countDead;
    }

    public Integer getCountWin() {
        return countWin;
    }

    public Integer getBibaCoinsCount() {
        return bibaCoinsCount;
    }

    public Long getLastMoveTime() {
        return lastMoveTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDto entity = (PlayerDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.colorName, entity.colorName) &&
                Objects.equals(this.colorBgName, entity.colorBgName) &&
                Objects.equals(this.colorBody, entity.colorBody) &&
                Objects.equals(this.HP, entity.HP) &&
                Objects.equals(this.speed, entity.speed) &&
                Objects.equals(this.isMove, entity.isMove) &&
                Objects.equals(this.vision, entity.vision) &&
                Objects.equals(this.countDead, entity.countDead) &&
                Objects.equals(this.countWin, entity.countWin) &&
                Objects.equals(this.bibaCoinsCount, entity.bibaCoinsCount) &&
                Objects.equals(this.lastMoveTime, entity.lastMoveTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, colorName, colorBgName, colorBody, HP, speed, isMove, vision, countDead, countWin, bibaCoinsCount, lastMoveTime);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "colorName = " + colorName + ", " +
                "colorBgName = " + colorBgName + ", " +
                "colorBody = " + colorBody + ", " +
                "HP = " + HP + ", " +
                "speed = " + speed + ", " +
                "isMove = " + isMove + ", " +
                "vision = " + vision + ", " +
                "countDead = " + countDead + ", " +
                "countWin = " + countWin + ", " +
                "bibaCoinsCount = " + bibaCoinsCount + ", " +
                "lastMoveTime = " + lastMoveTime + ")";
    }
}