package com.example.alef.core.command;

import com.example.alef.core.location.Position;
import com.example.alef.core.player.Player;

public class AttackCommand implements GameCommand {
    private Player attacker;
    private Position targetPosition;
    private Integer delayTicks = 5;

    @Override
    public Integer getDelayTicks() {
        return this.delayTicks;
    }

    @Override
    public void setDelayTicks(Integer delayTicks) {
        this.delayTicks=delayTicks;
    }

    public Player getAttacker() {
        return attacker;
    }

    public void setAttacker(Player attacker) {
        this.attacker = attacker;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

}

