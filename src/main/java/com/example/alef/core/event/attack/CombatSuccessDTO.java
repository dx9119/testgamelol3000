package com.example.alef.core.event.attack;


import java.util.List;

public class CombatSuccessDTO {
    private String attackerName;
    private List<TargetHit> hits;

    public CombatSuccessDTO(String attackerName, List<TargetHit> hits) {
        this.attackerName = attackerName;
        this.hits = hits;
    }

    public String getAttackerName() { return attackerName; }
    public List<TargetHit> getHits() { return hits; }

    public static class TargetHit {
        private String targetName;
        private long damage;
        private long remainingHP;

        public TargetHit(String targetName, long damage, long remainingHP) {
            this.targetName = targetName;
            this.damage = damage;
            this.remainingHP = remainingHP;
        }

        @Override
        public String toString() {
            return "TargetHit{" +
                    "targetName='" + targetName + '\'' +
                    ", damage=" + damage +
                    ", remainingHP=" + remainingHP +
                    '}';
        }

        public String getTargetName() { return targetName; }
        public long getDamage() { return damage; }
        public long getRemainingHP() { return remainingHP; }
    }
}



