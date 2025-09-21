package com.example.alef.core.market;

import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import com.example.alef.core.player.Weapon;
import com.example.alef.core.player.WeaponRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MarketService {

    private final WeaponRepo weaponRepo;
    private final MarketRepo marketRepo;
    private final PlayerRepo playerRepo;

    public MarketService(WeaponRepo weaponRepo, MarketRepo marketRepo, PlayerRepo playerRepo) {
        this.weaponRepo = weaponRepo;
        this.marketRepo = marketRepo;
        this.playerRepo = playerRepo;
    }

    @Transactional
    public BuyDTO buyWeapon(Market market, Player player, Long id) throws NotFoundException {
        BuyDTO buyDTO = new BuyDTO();
        Set<Long> weapons = market.getWeapons();

        if(weapons.contains(id)){
            Weapon weapon = weaponRepo.findById(id).orElseThrow(
                    NotFoundException::new
            );

            int currentCoins = player.getBibaCoinsCount();
            int cost = weapon.getCost();

            // Проверка: хватает ли монет
            if (currentCoins < cost) {
                buyDTO.setSellCost(cost);
                buyDTO.setIdWeapon(weapon.getId());
                buyDTO.setStatus("Не хватает Biba Coins на покупку.");
                return buyDTO;
            }

            // Списание монет
            player.setBibaCoinsCount(currentCoins - cost);

            player.setWeapon(weapon);
            playerRepo.save(player);

            weapons.remove(id);
            market.setWeapons(weapons);
            marketRepo.save(market);

            buyDTO.setSellCost(cost);
            buyDTO.setIdWeapon(weapon.getId());
            buyDTO.setStatus("Покупка совершена");
            return buyDTO;

        }
        buyDTO.setSellCost(null);
        buyDTO.setIdWeapon(null);
        buyDTO.setStatus("Нет такой пушки");
        return buyDTO;

    }

    @Transactional
    public SellInMarketDTO sellWeapon(Market market, Player player, Long id) throws NotFoundException {
        SellInMarketDTO sellDTO = new SellInMarketDTO();

        // Проверка: есть ли такое оружие у игрока
        Weapon weapon = weaponRepo.findById(id).orElseThrow(
                NotFoundException::new
        );

        if (weapon.getCost() == null) {
            sellDTO.setIdWeapon(id);
            sellDTO.setSellCost(0);
            sellDTO.setStatus("Нельзя продать стартовую пушку, только заменить на новую");
            return sellDTO;
        }

        if (player.getWeapon() == null || !player.getWeapon().getId().equals(id)) {
            sellDTO.setIdWeapon(id);
            sellDTO.setSellCost(0);
            sellDTO.setStatus("У игрока нет такого оружия");
            return sellDTO;
        }

        int sellCost = weapon.getCost();

        // Возврат монет игроку
        player.setBibaCoinsCount(player.getBibaCoinsCount() + sellCost);

        // Удаление оружия у игрока
        player.setWeapon(null);
        playerRepo.save(player);

        // Добавление оружия на рынок
        Set<Long> weapons = market.getWeapons();
        weapons.add(id);
        market.setWeapons(weapons);
        marketRepo.save(market);

        // Заполнение DTO
        sellDTO.setIdWeapon(id);
        sellDTO.setSellCost(sellCost);
        sellDTO.setStatus("Оружие успешно продано");
        return sellDTO;
    }

}
