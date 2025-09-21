package com.example.alef.core.enemy;

import com.example.alef.core.location.Position;
import com.example.alef.core.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EnemyRepo extends JpaRepository<Enemy,Long> {
    Set<Enemy> findByPositionXAndPositionY(int x, int y);
    Set<Enemy> findAllByLiveFalseAndTimeOfRespBefore(Instant now);
}
