package com.example.alef.core.location;

import com.example.alef.core.enemy.EnemyDTO;

import java.util.ArrayList;
import java.util.List;

public class CellDTO {
    private Long id;
    private int x;
    private int y;
    private Boolean barrier;
    private boolean isEmpty;
    private Long objectOnCellId;
    private String objectOnCellName;

    public CellDTO(Long id, int x, int y, Boolean barrier, boolean isEmpty,
                   Long objectOnCellId, String objectOnCellName, List<EnemyDTO> enemies) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.barrier = barrier;
        this.isEmpty = isEmpty;
        this.objectOnCellId = objectOnCellId;
        this.objectOnCellName = objectOnCellName;
    }

    public static CellDTO from(Cell cell, List<EnemyDTO> enemies) {
        Position pos = cell.getPosition();
        Long objectId = cell.getObjectOnCellId();
        boolean empty = (objectId == null) && (enemies == null || enemies.isEmpty());

        return new CellDTO(
                cell.getId(),
                pos.getX(),
                pos.getY(),
                cell.getBarrier(),
                empty,
                objectId,
                cell.getObjectOnCellName(),
                enemies
        );
    }

    public static CellDTO from(Cell cell) {
        return from(cell, new ArrayList<>());
    }

    public Long getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Boolean getBarrier() { return barrier; }
    public boolean isEmpty() { return isEmpty; }
    public Long getObjectOnCellId() { return objectOnCellId; }
    public String getObjectOnCellName() { return objectOnCellName; }

    @Override
    public String toString() {
        return "CellDTO{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", barrier=" + barrier +
                ", isEmpty=" + isEmpty +
                ", objectOnCellId=" + objectOnCellId +
                ", objectOnCellName='" + objectOnCellName + '\'' +
                '}';
    }
}
