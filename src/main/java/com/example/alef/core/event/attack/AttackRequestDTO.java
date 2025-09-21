package com.example.alef.core.event.attack;

public class AttackRequestDTO {
    private int targetX;
    private int targetY;

    // Конструкторы
    public AttackRequestDTO() {}

    public AttackRequestDTO(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    // Геттеры и сеттеры
    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    @Override
    public String toString() {
        return "AttackRequestDTO{" +
                "targetX=" + targetX +
                ", targetY=" + targetY +
                '}';
    }
}
