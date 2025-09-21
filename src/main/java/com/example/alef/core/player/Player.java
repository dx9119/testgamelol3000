package com.example.alef.core.player;

import com.example.alef.core.inventory.PlayerItem;
import com.example.alef.core.location.Position;
import com.example.alef.core.object.Item;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String colorName;
    private String colorBgName;
    private String colorBody;
    private Position position = new Position(0, 0);
    private Long HP=10L;
    private Integer speed = 1;
    private boolean isMove = false;
    private Integer vision = 5;
    private Integer countDead = 0;
    private Integer countWin = 0;
    private Integer bibaCoinsCount = 1000;
    private Boolean online = false;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    private Level level;

    //сумка
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PlayerItem> inventory = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "weapon_id")
    private Weapon weapon;

    private Long lastMoveTick = 0L;

    public Long getLastMoveTick() {
        return lastMoveTick;
    }

    public void setLastMoveTick(Long lastMoveTick) {
        this.lastMoveTick = lastMoveTick;
    }


    public Weapon getWeapon() {
        return weapon;
    }

    public Integer getCountDead() {
        return countDead;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setCountDead(Integer countDead) {
        this.countDead = countDead;
    }

    public Integer getCountWin() {
        return countWin;
    }

    public List<PlayerItem> getInventory() {
        return inventory;
    }

    public void setInventory(List<PlayerItem> inventory) {
        this.inventory = inventory;
    }

    public void setCountWin(Integer countWin) {
        this.countWin = countWin;
    }

    public Integer getBibaCoinsCount() {
        return bibaCoinsCount;
    }

    public void setBibaCoinsCount(Integer bibaCoinsCount) {
        this.bibaCoinsCount = bibaCoinsCount;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Level getLevel() {
        return level;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorBgName() {
        return colorBgName;
    }

    public void setColorBgName(String colorBgName) {
        this.colorBgName = colorBgName;
    }

    public String getColorBody() {
        return colorBody;
    }

    public void setColorBody(String colorBody) {
        this.colorBody = colorBody;
    }


    public void setLevel(Level level) {
        this.level = level;
    }

    public Integer getVision() {
        return vision;
    }

    public Long getHP() {
        return HP;
    }

    public void setHP(Long HP) {
        this.HP = HP;
    }

    public void setVision(Integer vision) {
        this.vision = vision;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}