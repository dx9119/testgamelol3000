package com.example.alef.core.event.attack;

public class CombatKillDTO {
    private String message;
    private int experienceGained;

    public CombatKillDTO(String message, int experienceGained) {
        this.message = message;
        this.experienceGained = experienceGained;
    }

    public String getMessage() {
        return message;
    }

    public int getExperienceGained() {
        return experienceGained;
    }
}

