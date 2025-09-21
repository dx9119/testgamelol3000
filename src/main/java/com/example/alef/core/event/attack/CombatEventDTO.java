package com.example.alef.core.event.attack;

public class CombatEventDTO {
    private Long attackerId;
    private String attackerName;
    private Long targetId;
    private Long damage;
    private Long remainingHP;
    private boolean success;

    public CombatEventDTO(Long attackerId, Long targetId, Long damage, Long remainingHP, boolean success,String attackerName) {
        this.attackerId = attackerId;
        this.attackerName = attackerName;
        this.targetId = targetId;
        this.damage = damage;
        this.remainingHP = remainingHP;
        this.success = success;
    }

    public Long getAttackerId() {
        return attackerId;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAttackerId(Long attackerId) {
        this.attackerId = attackerId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getDamage() {
        return damage;
    }

    public void setDamage(Long damage) {
        this.damage = damage;
    }

    public Long getRemainingHP() {
        return remainingHP;
    }

    public void setRemainingHP(Long remainingHP) {
        this.remainingHP = remainingHP;
    }
}

