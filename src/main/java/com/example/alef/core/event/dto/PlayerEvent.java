package com.example.alef.core.event.dto;

public class PlayerEvent {
    private String type;
    private String message;

    // геттеры и сеттеры
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "PlayerEvent{type='" + type + "', message='" + message + "'}";
    }
}

