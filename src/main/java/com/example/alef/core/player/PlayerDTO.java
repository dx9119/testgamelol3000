package com.example.alef.core.player;

import com.example.alef.core.inventory.PlayerItemDTO;
import com.example.alef.core.location.Position;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PlayerDTO {
    private Long id;
    private String name;
    private int x;
    private int y;

    @JsonProperty("isMoving")
    private boolean moving;

    private Long hp;
    private Integer speed;
    private Integer vision;
    private Long lastMoveTick;

    private Integer levelNumber;
    private Integer experience;

    private String colorName;
    private String colorBgName;
    private String colorBody;

    private Long weaponId;
    private List<PlayerItemDTO> inventory;

    private Integer countDead;
    private Integer countWin;
    private Integer bibaCoinsCount;

    private Boolean online;

    public PlayerDTO(Long id, String name, int x, int y, boolean moving,
                     Long hp, Integer speed, Integer vision, Long lastMoveTick,
                     Integer levelNumber, Integer experience,
                     String colorName, String colorBgName, String colorBody,
                     Long weaponId, List<PlayerItemDTO> inventory,
                     Integer countDead, Integer countWin, Integer bibaCoinsCount,
                     Boolean online) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.moving = moving;
        this.hp = hp;
        this.speed = speed;
        this.vision = vision;
        this.lastMoveTick = lastMoveTick;
        this.levelNumber = levelNumber;
        this.experience = experience;
        this.colorName = colorName;
        this.colorBgName = colorBgName;
        this.colorBody = colorBody;
        this.weaponId = weaponId;
        this.inventory = inventory;
        this.countDead = countDead;
        this.countWin = countWin;
        this.bibaCoinsCount = bibaCoinsCount;
        this.online = online;
    }

    public static PlayerDTO from(Player player) {
        Position pos = player.getPosition();
        Level level = player.getLevel();
        Weapon weapon = player.getWeapon();

        List<PlayerItemDTO> inventoryDTOs = player.getInventory().stream()
                .map(PlayerItemDTO::from)
                .toList();

        return new PlayerDTO(
                player.getId(),
                player.getName(),
                pos.getX(),
                pos.getY(),
                player.isMove(),
                player.getHP(),
                player.getSpeed(),
                player.getVision(),
                player.getLastMoveTick(),
                level != null ? level.getLevelNumber() : null,
                level != null ? level.getExperience() : null,
                player.getColorName(),
                player.getColorBgName(),
                player.getColorBody(),
                weapon != null ? weapon.getId() : null,
                inventoryDTOs,
                player.getCountDead(),
                player.getCountWin(),
                player.getBibaCoinsCount(),
                player.getOnline()
        );
    }

    // Геттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isMoving() { return moving; }
    public Long getHp() { return hp; }
    public Integer getSpeed() { return speed; }
    public Integer getVision() { return vision; }
    public Long getLastMoveTick() { return lastMoveTick; }
    public Integer getLevelNumber() { return levelNumber; }
    public Integer getExperience() { return experience; }
    public String getColorName() { return colorName; }
    public String getColorBgName() { return colorBgName; }
    public String getColorBody() { return colorBody; }
    public Long getWeaponId() { return weaponId; }
    public List<PlayerItemDTO> getInventory() { return inventory; }
    public Integer getCountDead() { return countDead; }
    public Integer getCountWin() { return countWin; }
    public Integer getBibaCoinsCount() { return bibaCoinsCount; }
    public Boolean getOnline() { return online; }
}
