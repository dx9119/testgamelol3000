package com.example.alef.core.player;

import com.example.alef.core.location.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlayerRepo extends JpaRepository<Player, Long> {
    Optional<Player> findByName(String name);
    Optional<Player> findByNameAndOnlineTrue(String name);
    Optional<Player> findByPositionAndOnlineTrue(Position position);
    Set<Player> findAllByPositionAndOnlineTrue(Position position);
    Set<Player> findAllByOnlineTrue();


}

