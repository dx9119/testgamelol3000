package com.example.alef.core.event.move;

import com.example.alef.core.location.Cell;
import com.example.alef.core.location.CellDTO;
import com.example.alef.core.location.CellRepo;
import com.example.alef.core.location.Position;
import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.alef.core.loop.GameLoop.gameTick;

@Service
public class MoveService {

    private final CellRepo cellRepo;
    private final PlayerRepo playerRepo;

    public MoveService(CellRepo cellRepo, PlayerRepo playerRepo) {
        this.cellRepo = cellRepo;
        this.playerRepo = playerRepo;
    }

    public Player processMove(Player player, MoveCommand command) {
        long currentTick = gameTick.get();
        long minDelayTicks = Math.max(1, 2 / player.getSpeed());

        if (currentTick - player.getLastMoveTick() < minDelayTicks) {
            throw new IllegalArgumentException("Клиент спамит командами движения");
        }

        player.setLastMoveTick(currentTick);


        Position current = player.getPosition();

        int x = current.getX();
        int y = current.getY();

        int targetX = x;
        int targetY = y;

        switch (command.getMoveTo()) {
            case U -> targetY = y - 1;
            case D -> targetY = y + 1;
            case L -> targetX = x - 1;
            case R -> targetX = x + 1;
        }


        Position targetPosition = new Position(targetX, targetY);

        // Проверка на препятствие по Cell
        Optional<Cell> cellOpt = cellRepo.findByPositionXAndPositionY(targetX, targetY);
        if (cellOpt.map(Cell::getBarrier).orElse(false)) {
            throw new IllegalArgumentException("Препятствие: барьер на клетке"+ CellDTO.from(cellOpt.get()));
        }

        Optional<Player> blockingPlayer = playerRepo.findByPositionAndOnlineTrue(targetPosition);

        // Всегда разрешаем движение через других игроков, но с задержкой
        if (blockingPlayer.isPresent() && !blockingPlayer.get().getId().equals(player.getId())) {
            long extraDelayTicks = 11; // 11 тиков = ~5.5 сек
            //добавить ивен по типу вы застряли
            player.setLastMoveTick(currentTick + extraDelayTicks);
        }

        // ВСЕГДА разрешаем движение на целевую клетку
        player.setPosition(targetPosition);
        return playerRepo.save(player);
    }


}
