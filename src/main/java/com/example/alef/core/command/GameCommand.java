package com.example.alef.core.command;

import com.example.alef.core.player.Player;

public interface GameCommand {
    Integer getDelayTicks();
    void setDelayTicks(Integer delayTicks);
}
