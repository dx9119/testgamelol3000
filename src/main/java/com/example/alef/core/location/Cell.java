package com.example.alef.core.location;

import jakarta.persistence.*;

@Entity
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long objectOnCellId;
    private String objectOnCellName;

    private Boolean barrier = true;

    @Version
    private Integer version;

    @Embedded
    private Position position;

    public String getObjectOnCellName() {
        return objectOnCellName;
    }

    public void setObjectOnCellName(String objectOnCellName) {
        this.objectOnCellName = objectOnCellName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getObjectOnCellId() {
        return objectOnCellId;
    }

    public void setObjectOnCellId(Long objectOnCellId) {
        this.objectOnCellId = objectOnCellId;
    }

    public Boolean getBarrier() {
        return barrier;
    }

    public void setBarrier(Boolean barrier) {
        this.barrier = barrier;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
