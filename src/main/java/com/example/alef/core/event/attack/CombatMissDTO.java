package com.example.alef.core.event.attack;

public class CombatMissDTO {
    private String attackerName;
    private MissReason missReason;

    public MissReason getMissReason() {
        return missReason;
    }

    public void setMissReason(MissReason missReason) {
        this.missReason = missReason;
    }

    public CombatMissDTO(String attackerName) {
        this.attackerName = attackerName;
    }

    // Геттер
    public String getAttackerName() { return attackerName; }
}
