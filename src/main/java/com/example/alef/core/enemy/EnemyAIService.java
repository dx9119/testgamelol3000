package com.example.alef.core.enemy;

import com.example.alef.config.GameLogger;
import com.example.alef.core.event.attack.AttackService;
import com.example.alef.core.event.attack.CombatNotifier;
import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import com.example.alef.core.player.PlayerStateRegistry;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;


@Service
public class EnemyAIService {

    private final EnemyRepo enemyRepo;
    private final PlayerStateRegistry playerStateRegistry;
    private final AttackService attackService;
    private final CombatNotifier combatNotifier;
    private final EnemyMoveService enemyMoveService;

    private final PlayerRepo playerRepo;

    public EnemyAIService(EnemyRepo enemyRepo,
                          PlayerStateRegistry playerStateRegistry,
                          AttackService attackService,
                          CombatNotifier combatNotifier, EnemyMoveService enemyMoveService,
                          PlayerRepo playerRepo) {
        this.enemyRepo = enemyRepo;
        this.playerStateRegistry = playerStateRegistry;
        this.attackService = attackService;
        this.combatNotifier = combatNotifier;
        this.enemyMoveService = enemyMoveService;
        this.playerRepo = playerRepo;
    }


    /**
     * Враг замечает игрока и начинает преследование.
     */
    public void markPlayerAsVisible(Enemy enemy, Player player) {
        // 1. Мертвый враг не реагирует
        if (!enemy.getLive()) {
            return;
        }

        int distanceToTarget = (int) Math.ceil(enemy.getPosition().distanceTo(player.getPosition()));

        int attackThreshold = enemy.getDistanceAttack() + 10; // Радиус атаки + запас
        int visionThreshold = Math.max(attackThreshold + 5, 30); // Зона видимости всегда больше зоны атаки

        // 1. Если игрок вне зоны видимости → сбрасываем цель
        if (distanceToTarget > visionThreshold) {
            enemy.setActive(false);
            enemy.setTargetPosition(null);
            enemy.setChaseStartTime(null);
            enemyRepo.save(enemy);
            GameLogger.log(enemy.getName() + ": потерял из вида " + player.getName());
            return;
        }

        // 2. Если игрок слишком далеко для атаки → просто наблюдаем
        if (distanceToTarget > attackThreshold) {
            GameLogger.log(enemy.getName() + ": Видит " + player.getName() + ", но слишком далеко для нападения");
            return;
        }

        // 3. Игрок в пределах атаки → можно преследовать или атаковать
        enemy.setTargetPosition(player.getPosition());
        enemy.setActive(true);
        enemy.setChaseStartTime(Instant.now());
        enemyRepo.save(enemy);
        GameLogger.log(enemy.getName() + ": начинает преследование " + player.getName());


        // длительность погони можно перезаписывать каждый раз
        enemy.setChaseDuration(Instant.now().plus(Duration.ofMinutes(3)));

        enemy.setActive(true);

        // Ограничение скорости движения врага
        long nowLong = System.currentTimeMillis();
        long minDelay = 10000L / Math.max(1, enemy.getSpeed());

        if (nowLong - enemy.getLastMoveTime() < minDelay) {
            return;
        }
        // сохраняем изменения и только потом инициируем движение
        enemy.setLastMoveTime(nowLong);
        enemyRepo.save(enemy);
        enemyMoveService.processMove(enemy);
    }



}
