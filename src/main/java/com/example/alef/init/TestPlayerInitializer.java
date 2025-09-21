package com.example.alef.init;

import com.example.alef.core.enemy.Enemy;
import com.example.alef.core.enemy.EnemyLoot;
import com.example.alef.core.enemy.EnemyLootRepo;
import com.example.alef.core.enemy.EnemyRepo;
import com.example.alef.core.location.Cell;
import com.example.alef.core.location.CellRepo;
import com.example.alef.core.location.Position;
import com.example.alef.core.object.GameObject;
import com.example.alef.core.object.GameObjectRepo;
import com.example.alef.core.object.Item;
import com.example.alef.core.object.ItemRepo;
import com.example.alef.core.player.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestPlayerInitializer implements CommandLineRunner {

    private final PlayerRepo playerRepo;
    private final LevelRepo levelRepo;
    private final GameObjectRepo gameObjectRepo;
    private final CellRepo cellRepo;
    private final ItemRepo itemRepo;
    private final EnemyRepo enemyRepo;
    private final EnemyLootRepo enemyLootRepo;
    private final WeaponRepo weaponRepo;
    private final BiomeService biomeService;

    private final TestMarketInitializer testMarketInitializer;

    public TestPlayerInitializer(PlayerRepo playerRepo,
                                 LevelRepo levelRepo,
                                 GameObjectRepo gameObjectRepo,
                                 CellRepo cellRepo,
                                 ItemRepo itemRepo,
                                 EnemyRepo enemyRepo,
                                 EnemyLootRepo enemyLootRepo, WeaponRepo weaponRepo, BiomeService biomeService,
                                 TestMarketInitializer testMarketInitializer) {
        this.playerRepo = playerRepo;
        this.levelRepo = levelRepo;
        this.gameObjectRepo = gameObjectRepo;
        this.cellRepo = cellRepo;
        this.itemRepo = itemRepo;
        this.enemyRepo = enemyRepo;
        this.enemyLootRepo = enemyLootRepo;
        this.weaponRepo = weaponRepo;
        this.biomeService = biomeService;
        this.testMarketInitializer = testMarketInitializer;
    }

    @Override
    public void run(String... args) {

        Weapon weapon = new Weapon();
        weaponRepo.save(weapon);
        Weapon weapon1 = new Weapon();
        weaponRepo.save(weapon1);

        // Игроки
        if (playerRepo.findByNameAndOnlineTrue("user1").isEmpty()) {
            Level level = new Level();
            Player user1 = new Player();
            user1.setName("user1");
            user1.setHP(10000L);
            user1.setLevel(level);
            user1.setWeapon(weapon);
            playerRepo.save(user1);
        }

        if (playerRepo.findByNameAndOnlineTrue("user2").isEmpty()) {
            Level level = new Level();
            Player user2 = new Player();
            user2.setName("user2");
            user2.setLevel(level);
            user2.setWeapon(weapon1);
            playerRepo.save(user2);
        }

        // Предмет
        Item item = new Item();
        item.setName("Metal");
        item.setDescription("Metal fragment");
        item.setRarity(100);
        item = itemRepo.save(item);

        List<Item> items = new ArrayList<>();
        items.add(item);


        // Враг : Skeleton Archer
        Enemy skeleton = new Enemy();
        skeleton.setName("Skeleton Archer");
        skeleton.setDamage(4);
        skeleton.setDistanceAttack(3);
        skeleton.setVisible(1);
        skeleton.setSpeed(10);
        skeleton.setLive(true);
        skeleton.setHP(8);
        skeleton.setPosition(new Position(-2, -2));
        skeleton.setTargetPosition(null);
        skeleton.setLastMoveTime(0L);
        skeleton.setChaseStartTime(null);
        skeleton.setChaseDuration(null);
        skeleton = enemyRepo.save(skeleton);

        EnemyLoot skeletonLoot = new EnemyLoot();
        skeletonLoot.setEnemy(skeleton);
        skeletonLoot.setItem(item);
        skeletonLoot.setDropChance(30);
        skeletonLoot.setQuantity(2);
        enemyLootRepo.save(skeletonLoot);

        biomeService.generateBiome(100,100, 1);
        testMarketInitializer.run();
    }
}
