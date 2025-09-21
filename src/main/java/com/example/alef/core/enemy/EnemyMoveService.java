package com.example.alef.core.enemy;

import com.example.alef.config.GameLogger;
import com.example.alef.core.location.Cell;
import com.example.alef.core.location.CellRepo;
import com.example.alef.core.location.Position;
import com.example.alef.core.event.attack.AttackService;
import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EnemyMoveService {

    private final CellRepo cellRepo;
    private final EnemyRepo enemyRepo;
    private final AttackService attackService;
    private final PlayerRepo playerRepo;

    public EnemyMoveService(CellRepo cellRepo, EnemyRepo enemyRepo, AttackService attackService, PlayerRepo playerRepo) {
        this.cellRepo = cellRepo;
        this.enemyRepo = enemyRepo;
        this.attackService = attackService;
        this.playerRepo = playerRepo;
    }

    // Главный метод движения врага
    public void processMove(Enemy enemy) {
        GameLogger.log("Враг " + enemy.getName() + " начал процесс движения");

        Position posCurrent = enemy.getPosition();
        GameLogger.log("Текущая позиция врага: " + posCurrent);

        Position posTarget = enemy.getTargetPosition();
        GameLogger.log("Целевая позиция врага: " + (posTarget != null ? posTarget : "цель отсутствует"));

        // Проверка наличия цели
        if (!hasTarget(posTarget, enemy)) {
            GameLogger.log("❌ Прерывание движения: у врага " + enemy.getName() + " нет цели");
            return;
        }

        if (canAttack(posCurrent, posTarget, enemy)) {
            performAttack(posTarget, enemy);
        }

        // Если дистанция слишком большая — двигается к цели
        GameLogger.log(" Враг " + enemy.getName() + " двигается к "+enemy.getTargetPosition().toString());


        // Вычисление следующего шага
        Position nextStep = calculateNextStep(posCurrent, posTarget);
        GameLogger.log("Враг " + enemy.getName() + " планирует шаг в сторону " + nextStep);

        // Проверка на барьер и попытка обойти
        Position resolvedStep = resolveBarrier(nextStep, enemy);
        GameLogger.log("Итоговая позиция после проверки барьеров: " + resolvedStep);

        // Если враг остался на месте — прерываем
        if (resolvedStep.equals(posCurrent)) {
            GameLogger.log(" Враг " + enemy.getName() + " остался на месте — движение отменено");
            return;
        }

        // Обновление позиции врага
        updateEnemyPosition(resolvedStep, enemy);
        GameLogger.log("Движение завершено: враг " + enemy.getName() + " теперь в " + resolvedStep);
    }


    // Проверка наличия цели у врага
    private boolean hasTarget(Position posTarget, Enemy enemy) {
        if (posTarget == null) {
            GameLogger.log("Враг " + enemy.getName() + " не имеет цели для движения");
            return false;
        }
        GameLogger.log(" Враг " + enemy.getName() + " имеет цель: " + posTarget);
        return true;
    }

    // Проверка, находится ли цель в радиусе атаки
    private boolean canAttack(Position posCurrent, Position posTarget, Enemy enemy) {
        int distanceToTarget = (int) Math.ceil(posCurrent.distanceTo(posTarget));
        int attackDistance = enemy.getDistanceAttack();
        GameLogger.log(" Расстояние до цели: " + distanceToTarget + ", дистанция атаки: " + attackDistance);
        return distanceToTarget <= attackDistance;
    }

    // Атака всех игроков на целевой позиции
    private void performAttack(Position posTarget, Enemy enemy) {
        GameLogger.log(" Враг " + enemy.getName() + " атакует позицию " + posTarget);
        Set<Player> players = playerRepo.findAllByPositionAndOnlineTrue(posTarget);
        for (Player player : players) {
            GameLogger.log(" Атака игрока " + player.getName() + " на позиции " + posTarget);
            attackService.processAttackEnemy(enemy, player, posTarget);
        }
    }


    // Вычисление следующего шага к цели — строго по одной оси
    private Position calculateNextStep(Position posCurrent, Position posTarget) {
        int dx = Integer.compare(posTarget.getX(), posCurrent.getX());
        int dy = Integer.compare(posTarget.getY(), posCurrent.getY());

        Position next;
        if (dx != 0) {
            next = new Position(posCurrent.getX() + dx, posCurrent.getY());
        } else if (dy != 0) {
            next = new Position(posCurrent.getX(), posCurrent.getY() + dy);
        } else {
            next = posCurrent; // уже на цели
        }

        GameLogger.log(" Вычислен следующий шаг врага: " + next);
        return next;
    }


    // Проверка на барьер и попытка найти обходной путь
    private Position resolveBarrier(Position nextStep, Enemy enemy) {
        Optional<Cell> cellOpt = cellRepo.findByPositionXAndPositionY(nextStep.getX(), nextStep.getY());
        if (!cellOpt.map(Cell::getBarrier).orElse(false)) return nextStep;

        GameLogger.log(" Барьер на пути врага " + enemy.getName() + " в " + nextStep);
        Position now = enemy.getPosition();

        List<Position> alternatives = new ArrayList<>(List.of(
                new Position(now.getX() + 1, now.getY()),
                new Position(now.getX() - 1, now.getY()),
                new Position(now.getX(), now.getY() + 1),
                new Position(now.getX(), now.getY() - 1)
        ));

        Collections.shuffle(alternatives);
        GameLogger.log(" Враг " + enemy.getName() + " ищет обходной путь среди: " + alternatives);

        for (Position alt : alternatives) {
            Optional<Cell> altCell = cellRepo.findByPositionXAndPositionY(alt.getX(), alt.getY());
            if (!altCell.map(Cell::getBarrier).orElse(false)) {
                GameLogger.log("Найден обходной путь: " + alt);
                return alt;
            }
        }

        GameLogger.log(" Все направления заблокированы, враг " + enemy.getName() + " остаётся на месте");
        return now;
    }

    // Сохранение новой позиции врага
    private void updateEnemyPosition(Position nextStep, Enemy enemy) {
        enemy.setPosition(nextStep);
        enemyRepo.save(enemy);
        GameLogger.log(" Враг " + enemy.getName() + " переместился в " + nextStep);
    }
}
