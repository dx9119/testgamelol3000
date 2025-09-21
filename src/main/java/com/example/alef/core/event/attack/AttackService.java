package com.example.alef.core.event.attack;

import com.example.alef.config.GameLogger;
import com.example.alef.core.enemy.Enemy;
import com.example.alef.core.enemy.EnemyRepo;
import com.example.alef.core.location.Cell;
import com.example.alef.core.location.CellRepo;
import com.example.alef.core.location.Position;
import com.example.alef.core.location.PositionDTO;
import com.example.alef.core.object.GameObject;
import com.example.alef.core.object.GameObjectRepo;
import com.example.alef.core.object.DestructionService;
import com.example.alef.core.player.*;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AttackService {

    private final PlayerRepo playerRepo;
    private final CellRepo cellRepo;
    private final GameObjectRepo gameObjectRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final LevelRepo levelRepo;
    private final DestructionService destructionService;
    private final EnemyRepo enemyRepo;

    public AttackService(PlayerRepo playerRepo, CellRepo cellRepo, GameObjectRepo gameObjectRepo,
                         SimpMessagingTemplate messagingTemplate, LevelRepo levelRepo,
                         DestructionService destructionService, EnemyRepo enemyRepo) {
        this.playerRepo = playerRepo;
        this.cellRepo = cellRepo;
        this.gameObjectRepo = gameObjectRepo;
        this.messagingTemplate = messagingTemplate;
        this.levelRepo = levelRepo;
        this.destructionService = destructionService;
        this.enemyRepo = enemyRepo;
    }

    public CombatSuccessDTO processAttack(Player attacker, Position targetPosition) {
        if (isTooFar(attacker, targetPosition)) {
            notifyMiss(attacker, MissReason.TOOFARAWAY);
            return null;
        }

        List<CombatSuccessDTO.TargetHit> hits = new ArrayList<>();

        long damage = damage(attacker);

        if (attacker.getWeapon().getAvailableEnergy() < attacker.getWeapon().getEnergyCostForAttack()){
            notifyMiss(attacker,MissReason.NOWEAPONENERGY);
            GameLogger.log(attacker.getName()+" Не хватает энергии на выстрел");
            return null;
        }

        List<Player> targets = getOtherPlayersAtPosition(attacker, targetPosition);
        handlePlayerHits(attacker, targets, damage, hits);
        handleObjectHit(attacker, targetPosition, damage, hits);
        handleEnemyHits(attacker, targetPosition, damage, hits);

        if (hits.isEmpty()) {
            notifyMiss(attacker, targets.isEmpty() ? MissReason.EMPTYCCELL : MissReason.SELFHARM);
            return null;
        }

        return new CombatSuccessDTO(attacker.getName(), hits);
    }

    public CombatSuccessDTO processAttackEnemy(Enemy enemy, Player player, Position targetPosition) {
        // Проверка дистанции атаки
        // player просто заглушка
        if (isTooFarForEnemy(enemy, player, targetPosition)) {
            Player info = new Player(); // временный объект для уведомления
            info.setName(enemy.getName());
            notifyMiss(info, MissReason.TOOFARAWAY);
            GameLogger.log(enemy.getName()+"Не достает до атаки по "+player.getName());
            return null;
        }

        List<CombatSuccessDTO.TargetHit> hits = new ArrayList<>();

        // Расчет урона врага
        long baseDamage = ThreadLocalRandom.current().nextLong(enemy.getDamage()-1, enemy.getDamage()+1);
        boolean isCrit = ThreadLocalRandom.current().nextInt(100) < 10; // 10% шанс крита
        double critMultiplier = isCrit ? 1.5 : 1.0;
        long damage = Math.round(baseDamage * critMultiplier);

        // Применение урона
        long newHP = Math.max(0, player.getHP() - damage);
        player.setHP(newHP);
        playerRepo.save(player);

        // Обработка смерти игрока
        if (newHP <= 0) {
            handlePlayerDeathFromEnemy(enemy, player);
        }

        // Создание DTO попадания
        CombatSuccessDTO.TargetHit hit = new CombatSuccessDTO.TargetHit(player.getName(), damage, newHP);
        hits.add(hit);

        // Отправка уведомлений
        messagingTemplate.convertAndSendToUser(player.getName(), "/queue/combat",
                new CombatSuccessDTO(enemy.getName(), List.of(hit)));
        GameLogger.log(hits.toString());
        return new CombatSuccessDTO(enemy.getName(), hits);
    }


    private long damage(Player attacker){
        Weapon weapon = attacker.getWeapon();

        // Базовый урон: случайное значение в диапазоне
        long baseDamage = ThreadLocalRandom.current().nextLong(weapon.getDownDamage(), weapon.getUpDamage() + 1);

        // Критический удар: 20% шанс, множитель 1.5x
        boolean isCrit = ThreadLocalRandom.current().nextInt(100) < 20;
        double critMultiplier = isCrit ? 1.5 : 1.0;

        // Бонус от уровня: мягкий рост
        int level = attacker.getLevel().getLevelNumber();
        double levelBonus = 1.0 + Math.sqrt(level) * 0.1;

        // Финальный урон
        long damage = Math.round(baseDamage * critMultiplier * levelBonus);
        return damage;
    }

    private boolean isTooFar(Player attacker, Position targetPosition) {
        int maxDistance = attacker.getWeapon().getDistance();
        return attacker.getPosition().distanceTo(targetPosition) > maxDistance * maxDistance;
    }

    private boolean isTooFarForEnemy(Enemy enemy,Player player, Position targetPosition) {
        int maxDistance = enemy.getDistanceAttack();
        return player.getPosition().distanceTo(targetPosition) > maxDistance * maxDistance;
    }

    private void notifyMiss(Player attacker, MissReason reason) {
        CombatMissDTO missDto = new CombatMissDTO(attacker.getName());
        missDto.setMissReason(reason);
        messagingTemplate.convertAndSendToUser(attacker.getName(), "/queue/combat", missDto);
    }

    private List<Player> getOtherPlayersAtPosition(Player attacker, Position position) {
        return playerRepo.findAllByPositionAndOnlineTrue(position).stream()
                .filter(p -> !p.getId().equals(attacker.getId()))
                .collect(Collectors.toList());
    }

    private void handlePlayerHits(Player attacker, List<Player> targets, long damage, List<CombatSuccessDTO.TargetHit> hits) {
        for (Player target : targets) {
            long newHP = Math.max(0, target.getHP() - damage);
            target.setHP(newHP);
            playerRepo.save(target);

            if (newHP <= 0) {
                handlePlayerDeath(attacker, target);
            }

            CombatSuccessDTO.TargetHit hit = new CombatSuccessDTO.TargetHit(target.getName(), damage, newHP);
            hits.add(hit);
            messagingTemplate.convertAndSendToUser(target.getName(), "/queue/combat",
                    new CombatSuccessDTO(attacker.getName(), List.of(hit)));
        }
    }

    private void handlePlayerDeath(Player attacker, Player target) {
        target.setCountDead(target.getCountDead() + 1);
        target.setHP(10L * target.getLevel().getLevelNumber());

        // Назначаем новую свободную позицию
        Position respawnPosition = findFreeRandomPosition();
        target.setPosition(respawnPosition);
        playerRepo.save(target);

        attacker.setCountWin(attacker.getCountWin() + 1);
        Level level = attacker.getLevel();
        int expGained = calculateExpGain(target);
        level.setExperience(level.getExperience() + expGained);
        level.levelUp();
        levelRepo.save(level);
        playerRepo.save(attacker);

        messagingTemplate.convertAndSendToUser(target.getName(), "/queue/combat",
                new CombatKillDTO("Вы были убиты игроком: " + attacker.getName(), 0));
        messagingTemplate.convertAndSendToUser(attacker.getName(), "/queue/combat",
                new CombatKillDTO("Вы убили игрока: " + target.getName(), expGained));
    }

    @Transactional
    private void handlePlayerDeathFromEnemy(Enemy enemy, Player player) {
        // Обновляем игрока
        player.setCountDead(player.getCountDead() + 1);
        player.setHP(10L * player.getLevel().getLevelNumber());
        Position respawnPosition = findFreeRandomPosition();
        player.setPosition(respawnPosition);
        playerRepo.save(player);
        // Сообщаем клиенту актуальные координаты
        PositionDTO currentPlayerPosition = PositionDTO.from(player.getPosition());
        messagingTemplate.convertAndSendToUser(player.getName(), "/queue/events", currentPlayerPosition);

        // Сбрасываем врага
        //enemy.setLive(false);
        //enemy.setActive(false);
        enemy.setTargetPosition(null);
        enemy.setLastMeetWithPlayer(null);
        enemy.setChaseStartTime(null);
        enemy.setChaseDuration(null);

        enemyRepo.save(enemy);

        GameLogger.log(enemy.getName() + " Убил и успокоился, уничтожен: " + player.getName());

        // Сообщение игроку
        messagingTemplate.convertAndSendToUser(
                player.getName(),
                "/queue/combat",
                new CombatKillDTO("Вы были убиты существом: " + enemy.getName(), 0)
        );
    }


    private Position findFreeRandomPosition() {
        int attempts = 1000;

        for (int i = 0; i < attempts; i++) {
            int x = ThreadLocalRandom.current().nextInt(-100, 101);
            int y = ThreadLocalRandom.current().nextInt(-100, 101);
            Position candidate = new Position(x, y);

            Optional<Cell> cellOpt = cellRepo.findByPositionXAndPositionY(x, y);

            if (cellOpt.isEmpty()) {
                return candidate;
            }

            Cell cell = cellOpt.get();
            boolean occupied = cell.getObjectOnCellId() != null || Boolean.TRUE.equals(cell.getBarrier());

            if (!occupied) {
                return candidate;
            }
        }

        System.out.println("⚠️ Не удалось найти свободную позицию, используем (0,0)");
        return new Position(0, 0);
    }


    private int calculateExpGain(Player victim) {
        int baseExp = 50;
        int levelBonus = (int) Math.sqrt(victim.getLevel().getLevelNumber()) * 5;
        return baseExp + levelBonus;
    }

    private void handleObjectHit(Player attacker, Position position, long damage, List<CombatSuccessDTO.TargetHit> hits) {
        Optional<Cell> cellOpt = cellRepo.findByPositionXAndPositionY(position.getX(), position.getY());
        if (cellOpt.isEmpty()) return;

        Long objectId = cellOpt.get().getObjectOnCellId();
        if (objectId == null) return;

        Optional<GameObject> objectOpt = gameObjectRepo.findById(objectId);
        if (objectOpt.isEmpty()) return;

        GameObject obj = objectOpt.get();
        if (!Boolean.TRUE.equals(obj.getVisible()) || Boolean.TRUE.equals(obj.getDestroy())) return;

        long newHP = Math.max(0, obj.getHP() - damage);
        obj.setHP(newHP);
        if (newHP == 0) {
            destructionService.destroyObject(obj, attacker, cellOpt.get());
        }
        gameObjectRepo.save(obj);

        CombatSuccessDTO.TargetHit hit = new CombatSuccessDTO.TargetHit(obj.getName(), damage, newHP);
        hits.add(hit);
    }

    private void handleEnemyHits(Player attacker, Position position, long damage, List<CombatSuccessDTO.TargetHit> hits) {
        Set<Enemy> enemies = enemyRepo.findByPositionXAndPositionY(position.getX(), position.getY());

        for (Enemy enemy : enemies) {
            if (!enemy.getLive()) {
                System.out.printf("⚠️ Враг %s уже мертв\n", enemy.getName());
                continue;
            }

            int newHP = Math.max(0, enemy.getHP() - (int) damage);
            enemy.setHP(newHP);
            enemyRepo.save(enemy);

            if (newHP <= 0) {
                System.out.printf("💀 Враг %s убит игроком %s\n", enemy.getName(), attacker.getName());
                destructionService.killEnemy(enemy, attacker);
                messagingTemplate.convertAndSendToUser(attacker.getName(), "/queue/combat",
                        new CombatKillDTO("Вы убили врага: " + enemy.getName(), 0));
            }

            CombatSuccessDTO.TargetHit hit = new CombatSuccessDTO.TargetHit(enemy.getName(), damage, (long) newHP);
            hits.add(hit);
            messagingTemplate.convertAndSendToUser(attacker.getName(), "/queue/combat",
                    new CombatSuccessDTO(attacker.getName(), List.of(hit)));
        }
    }
}
