package com.example.alef.config.ws;

import com.example.alef.core.location.Position;

public class HelloUserMessage {
    private String username;
    private Position playerPosition;

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Position playerPosition) {
        this.playerPosition = playerPosition;
    }

    public HelloUserMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
