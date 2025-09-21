package com.example.alef.core.object;

import com.example.alef.core.enemy.*;
import com.example.alef.core.inventory.PlayerItem;
import com.example.alef.core.location.Cell;
import com.example.alef.core.location.CellRepo;
import com.example.alef.core.location.Position;
import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DestructionService {

    // Зависимости, внедряемые через конструктор
    private final GameObjectRepo gameObjectRepo;
    private final PlayerRepo playerRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final EnemyRepo enemyRepo;
    private final CellRepo cellRepo;

    public DestructionService(GameObjectRepo gameObjectRepo,
                              PlayerRepo playerRepo,
                              ItemRepo itemRepo,
                              SimpMessagingTemplate messagingTemplate,
                              EnemyLootRepo enemyLootRepo,
                              EnemyRepo enemyRepo, CellRepo cellRepo) {
        this.gameObjectRepo = gameObjectRepo;
        this.playerRepo = playerRepo;
        this.messagingTemplate = messagingTemplate;
        this.enemyRepo = enemyRepo;
        this.cellRepo = cellRepo;
    }

    /**
     * Обрабатывает разрушение игрового объекта и выпадение лута.
     * @param object — объект, который был уничтожен
     * @param destroyer — игрок, который его уничтожил
     */
    @Transactional
    public void destroyObject(GameObject object, Player destroyer, Cell cell) {
        if (object.getDestroy() != null && object.getDestroy()) {
            System.out.printf("⚠️ Объект %s уже уничтожен\n", object.getName());
            return;
        }

        markObjectDestroyed(object);
        List<Item> droppedItems = rollLootFromObject(object, destroyer);
        playerRepo.save(destroyer);
        cell.setBarrier(false);
        cellRepo.save(cell);

        notifyLootResult(destroyer, object.getName(), droppedItems);
    }

    /**
     * Обрабатывает уничтожение врага и выпадение лута.
     * @param enemy — враг, который был убит
     * @param killer — игрок, который его убил
     */
    @Transactional
    public void killEnemy(Enemy enemy, Player killer) {
        if (enemy.getLive() == null || !enemy.getLive()) {
            System.out.printf("⚠️ Враг %s уже мертв\n", enemy.getName());
            return;
        }

        markEnemyKilled(enemy);
        List<Item> droppedItems = rollLootFromEnemy(enemy, killer);
        playerRepo.saveAndFlush(killer);
        notifyLootResult(killer, enemy.getName(), droppedItems);
    }

    // Помечает объект как уничтоженный
    @Transactional
    private void markObjectDestroyed(GameObject object) {
        object.setDestroy(true);
        object.setTimeOfdestroy(Instant.now());
        gameObjectRepo.save(object);
        System.out.printf("💥 Объект %s уничтожен\n", object.getName());
    }

    // Помечает врага как убитого
    @Transactional
    private void markEnemyKilled(Enemy enemy) {
        enemy.setLive(false);
        enemy.setHP(0);
        enemyRepo.save(enemy);
        System.out.printf("💀 Враг %s уничтожен\n", enemy.getName());
    }

    // Генерация лута из объекта
    private List<Item> rollLootFromObject(GameObject object, Player destroyer) {
        List<Item> droppedItems = new ArrayList<>();

        for (Item item : object.getLootTable()) {
            int chance = item.getRarity();
            int roll = ThreadLocalRandom.current().nextInt(100);
            System.out.printf("🎲 Лут: %s (ID=%d), шанс=%d%%, выпадение=%s\n",
                    item.getName(), item.getId(), chance, roll < chance ? "ДА" : "НЕТ");

            if (roll < chance) {
                droppedItems.add(item);
                addItemToInventory(destroyer, item, 1);
            }
        }

        return droppedItems;
    }

    // Генерация лута из врага
    private List<Item> rollLootFromEnemy(Enemy enemy, Player killer) {
        List<Item> droppedItems = new ArrayList<>();

        for (EnemyLoot loot : enemy.getLootTable()) {
            Item item = loot.getItem();
            int chance = loot.getDropChance();
            int roll = ThreadLocalRandom.current().nextInt(100);
            boolean dropped = roll < chance;

            System.out.printf("🎲 Лут: %s (ID=%d), шанс=%d%%, выпадение=%s\n",
                    item.getName(), item.getId(), chance, dropped ? "ДА" : "НЕТ");

            if (dropped) {
                droppedItems.add(item);
                addItemToInventory(killer, item, loot.getQuantity());
            }
        }

        return droppedItems;
    }

    // Добавление предмета в инвентарь игрока
    private void addItemToInventory(Player player, Item item, int count) {
        for (PlayerItem pi : player.getInventory()) {
            if (pi.getItem() != null && pi.getItem().getId().equals(item.getId())) {
                int oldCount = pi.getCount();
                pi.setCount(oldCount + count);
                System.out.printf("🔄 Обновлено: %s (ID=%d), было=%d, стало=%d\n",
                        item.getName(), item.getId(), oldCount, pi.getCount());
                return;
            }
        }

        PlayerItem newItem = new PlayerItem();
        newItem.setPlayer(player);
        newItem.setItem(item);
        newItem.setCount(count);
        player.getInventory().add(newItem);

        System.out.printf("🆕 Добавлен: %s (ID=%d), кол-во=%d\n",
                item.getName(), item.getId(), count);
    }

    // Отправка уведомления игроку о полученном луте
    private void notifyLootResult(Player player, String sourceName, List<Item> droppedItems) {
        if (!droppedItems.isEmpty()) {
            String lootMessage = String.format("Получено из %s!", sourceName);
            LootDTO lootDTO = new LootDTO(sourceName, droppedItems, lootMessage);
            messagingTemplate.convertAndSendToUser(player.getName(), "/queue/loot", lootDTO);
            System.out.printf("📦 Игрок %s получил %d предметов от %s\n", player.getName(), droppedItems.size(), sourceName);
        } else {
            System.out.printf("📭 Игрок %s не получил лут от %s\n", player.getName(), sourceName);
        }
    }
}
