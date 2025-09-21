package com.example.alef.core.player;

import com.example.alef.core.location.Position;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OtherPlayerDTO {
    private String name;
    private int x;
    private int y;

    @JsonProperty("isMoving")
    private boolean moving;

    private Long hp;
    private Integer speed;

    private String colorName;
    private String colorBgName;
    private String colorBody;

    private Long weaponId;

    public OtherPlayerDTO(String name, int x, int y, boolean moving,
                          Long hp, Integer speed,
                          String colorName, String colorBgName, String colorBody,
                          Long weaponId) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.moving = moving;
        this.hp = hp;
        this.speed = speed;
        this.colorName = colorName;
        this.colorBgName = colorBgName;
        this.colorBody = colorBody;
        this.weaponId = weaponId;
    }

    public OtherPlayerDTO() {}

    public static OtherPlayerDTO from(Player player) {
        Position pos = player.getPosition();
        Weapon weapon = player.getWeapon();

        return new OtherPlayerDTO(
                player.getName(),
                pos.getX(),
                pos.getY(),
                player.isMove(),
                player.getHP(),
                player.getSpeed(),
                player.getColorName(),
                player.getColorBgName(),
                player.getColorBody(),
                weapon != null ? weapon.getId() : null
        );
    }

    // Геттеры
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isMoving() { return moving; }
    public Long getHp() { return hp; }
    public Integer getSpeed() { return speed; }
    public String getColorName() { return colorName; }
    public String getColorBgName() { return colorBgName; }
    public String getColorBody() { return colorBody; }
    public Long getWeaponId() { return weaponId; }

    @Override
    public String toString() {
        return "OtherPlayerDTO{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", isMoving=" + moving +
                ", hp=" + hp +
                ", speed=" + speed +
                ", colorName='" + colorName + '\'' +
                ", colorBgName='" + colorBgName + '\'' +
                ", colorBody='" + colorBody + '\'' +
                ", weaponId=" + weaponId +
                '}';
    }
}

