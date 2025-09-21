package com.example.alef.core.enemy;

import com.example.alef.core.location.Position;

public class EnemyDTO {
    private Long id;
    private String name;
    private Integer HP;
    private Integer damage;
    private Position position;
    private Boolean live;


    public EnemyDTO(Long id, String name, Integer HP, Integer damage, Position position, Boolean live) {
        this.id = id;
        this.name = name;
        this.HP = HP;
        this.damage = damage;
        this.position = position;
        this.live = live;
    }

    public static EnemyDTO from(Enemy enemy) {
        return new EnemyDTO(
                enemy.getId(),
                enemy.getName(),
                enemy.getHP(),
                enemy.getDamage(),
                enemy.getPosition(),
                enemy.getLive()
        );
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getHP() { return HP; }
    public Integer getDamage() { return damage; }
    public Position getPosition() { return position; }

    public void setLive(Boolean live) {
        this.live = live;
    }

    public Boolean getLive() {
        return live;
    }
}

