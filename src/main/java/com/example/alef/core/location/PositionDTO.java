package com.example.alef.core.location;

public class PositionDTO {
    private int x;
    private int y;

    public static PositionDTO from(Position position) {
        PositionDTO dto = new PositionDTO();
        dto.setX(position.getX());
        dto.setY(position.getY());
        return dto;
    }

    @Override
    public String toString() {
        return "PositionDTO{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}