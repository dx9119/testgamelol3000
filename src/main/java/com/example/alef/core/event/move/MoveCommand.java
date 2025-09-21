package com.example.alef.core.event.move;

import com.example.alef.core.command.GameCommand;

public class MoveCommand implements GameCommand {
    private StepDirection stepDirection;
    private Integer delayTicks = 0;

    @Override
    public Integer getDelayTicks() {
        return delayTicks;
    }

    @Override
    public void setDelayTicks(Integer delayTicks) {
        this.delayTicks = delayTicks;
    }

    public StepDirection getMoveTo() {
        return stepDirection;
    }

    public void setMoveTo(StepDirection stepDirection) {
        this.stepDirection = stepDirection;
    }
}
