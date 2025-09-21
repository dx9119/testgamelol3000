package com.example.alef.core.enemy;

import com.example.alef.core.location.Position;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Enemy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private Integer damage=1;
    private Integer distanceAttack=2;
    private Integer visible;
    private Integer speed=1;
    @OneToMany(mappedBy = "enemy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EnemyLoot> lootTable = new ArrayList<>();
    private Position position = new Position(0, 0);
    private Boolean live=true;
    private Integer HP;
    private Long lastMoveTime = 0L;
    private Instant lastMeetWithPlayer;
    // дата смерти и респауна
    private Instant timeOfDead;
    private Instant timeOfResp;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "target_x")),
            @AttributeOverride(name = "y", column = @Column(name = "target_y"))
    })
    private Position targetPosition;
    private Instant chaseStartTime;     // когда начал преследование
    private Instant chaseDuration;    // сколько длится преследование
    private Boolean active=false;   // в погоне он за игроком или нет

    public Boolean getActive() {
        return active;
    }

    public Instant getLastMeetWithPlayer() {
        return lastMeetWithPlayer;
    }

    public Instant getTimeOfDead() {
        return timeOfDead;
    }


    public void setTimeOfDead(Instant timeOfDead) {
        this.timeOfDead = timeOfDead;
    }

    public Instant getTimeOfResp() {
        return timeOfResp;
    }

    public void setTimeOfResp(Instant timeOfResp) {
        this.timeOfResp = timeOfResp;
    }

    public void setLastMeetWithPlayer(Instant lastMeetWithPlayer) {
        this.lastMeetWithPlayer = lastMeetWithPlayer;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getChaseStartTime() {
        return chaseStartTime;
    }

    public void setChaseStartTime(Instant chaseStartTime) {
        this.chaseStartTime = chaseStartTime;
    }

    public Instant getChaseDuration() {
        return chaseDuration;
    }

    public void setChaseDuration(Instant chaseDuration) {
        this.chaseDuration = chaseDuration;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public Long getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(Long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", damage=" + damage +
                ", distanceAttack=" + distanceAttack +
                ", visible=" + visible +
                ", speed=" + speed +
                ", lootTable=" + lootTable +
                ", position=" + position +
                ", live=" + live +
                ", HP=" + HP +
                ", lastMoveTime=" + lastMoveTime +
                ", lastMeetWithPlayer=" + lastMeetWithPlayer +
                ", timeOfDead=" + timeOfDead +
                ", timeOfResp=" + timeOfResp +
                ", targetPosition=" + targetPosition +
                ", chaseStartTime=" + chaseStartTime +
                ", chaseDuration=" + chaseDuration +
                ", active=" + active +
                '}';
    }

    public Integer getDistanceAttack() {
        return distanceAttack;
    }

    public void setDistanceAttack(Integer distanceAttack) {
        this.distanceAttack = distanceAttack;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public List<EnemyLoot> getLootTable() {
        return lootTable;
    }

    public void setLootTable(List<EnemyLoot> lootTable) {
        this.lootTable = lootTable;
    }

    public Boolean getLive() {
        return live;
    }

    public void setLive(Boolean live) {
        this.live = live;
    }

    public Integer getHP() {
        return HP;
    }

    public void setHP(Integer HP) {
        this.HP = HP;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
