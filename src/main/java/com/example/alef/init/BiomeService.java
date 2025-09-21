package com.example.alef.init;

import com.example.alef.core.location.Cell;
import com.example.alef.core.location.CellRepo;
import com.example.alef.core.location.Position;
import com.example.alef.core.object.GameObject;
import com.example.alef.core.object.GameObjectRepo;
import com.example.alef.core.object.Item;
import com.example.alef.core.object.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class BiomeService {

    @Autowired
    private GameObjectRepo gameObjectRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private CellRepo cellRepo;

    private final Random random = new Random();

    // Список возможных названий для игровых объектов
    private final List<String> objectNames = Arrays.asList("Tree", "Rock", "Bush", "Cactus", "Stump");

    // Список возможных названий для предметов
    private final List<String> itemNames = Arrays.asList("Metal", "Wood", "Stone", "Crystal", "Fiber");

    // Диапазон цен для предметов
    private final int MIN_ITEM_RARITY = 50;
    private final int MAX_ITEM_RARITY = 200;

    /**
     * Генерирует биом с сеткой клеток, содержащих игровые объекты.
     * Пустые клетки не создаются, только клетки с объектами.
     *
     * @param width         Ширина сетки биома
     * @param height        Высота сетки биома
     * @param objectDensity Вероятность (0 до 1) появления объекта на клетке
     * @return Список созданных клеток с объектами
     */
    public List<Cell> generateBiome(int width, int height, double objectDensity) {
        List<Cell> cells = new ArrayList<>();
        List<Item> items = createRandomItems();

        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                // Проверяем, нужно ли создать объект на этой позиции
                if (random.nextDouble() < objectDensity) {
                    GameObject gameObject = createRandomGameObject(items);
                    Cell cell = new Cell();
                    cell.setPosition(new Position(x, y));
                    cell.setObjectOnCellId(gameObject.getId());
                    cell.setObjectOnCellName(gameObject.getName());
                    cell.setBarrier(true);
                    cells.add(cellRepo.save(cell));
                }
                // Если объект не создается, клетка не создается
            }
        }

        return cells;
    }

    /**
     * Создает список предметов с случайными названиями и ценами.
     *
     * @return Список созданных предметов
     */
    private List<Item> createRandomItems() {
        List<Item> items = new ArrayList<>();
        // Создаем 1-3 случайных предмета для лута
        int itemCount = random.nextInt(3) + 1;
        for (int i = 0; i < itemCount; i++) {
            Item item = new Item();
            String itemName = itemNames.get(random.nextInt(itemNames.size()));
            item.setName(itemName);
            item.setDescription(itemName + " fragment");
            item.setRarity(MIN_ITEM_RARITY + random.nextInt(MAX_ITEM_RARITY - MIN_ITEM_RARITY + 1));
            items.add(itemRepo.save(item));
        }
        return items;
    }

    /**
     * Создает игровой объект с случайным названием и лутом.
     *
     * @param items Предметы для таблицы лута
     * @return Созданный игровой объект
     */
    private GameObject createRandomGameObject(List<Item> items) {
        GameObject gameObject = new GameObject();
        String objectName = objectNames.get(random.nextInt(objectNames.size()));
        gameObject.setName(objectName);
        gameObject.setHP(2L + random.nextInt(3)); // HP от 2 до 4
        gameObject.setVisible(true);
        gameObject.setDestroy(false);
        gameObject.setLootTable(items);
        return gameObjectRepo.save(gameObject);
    }
}