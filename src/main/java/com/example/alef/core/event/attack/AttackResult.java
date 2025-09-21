package com.example.alef.core.event.attack;

import com.example.alef.core.player.Player;

public class AttackResult {
    private Player attacker;
    private Player target;
    private long damage;
    private long remainingHP;
    private boolean success;
    private String attackerName;

    public AttackResult(Player attacker, Player target, long damage, long remainingHP, boolean success, String attackerName) {
        this.attacker = attacker;
        this.target = target;
        this.damage = damage;
        this.remainingHP = remainingHP;
        this.success = success;
        this.attackerName = attackerName;
    }

    public Player getAttacker() {
        return attacker;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAttacker(Player attacker) {
        this.attacker = attacker;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public long getDamage() {
        return damage;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }

    public long getRemainingHP() {
        return remainingHP;
    }

    public void setRemainingHP(long remainingHP) {
        this.remainingHP = remainingHP;
    }
}

