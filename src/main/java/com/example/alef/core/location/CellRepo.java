package com.example.alef.core.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CellRepo extends JpaRepository<Cell,Long> {
    Optional<Cell> findByPositionXAndPositionY(int x, int y);
}
