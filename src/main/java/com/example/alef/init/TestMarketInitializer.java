package com.example.alef.init;

import com.example.alef.core.market.Market;
import com.example.alef.core.market.MarketRepo;
import com.example.alef.core.player.Weapon;
import com.example.alef.core.player.WeaponRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestMarketInitializer implements CommandLineRunner {

    private final MarketRepo marketRepo;
    private final WeaponRepo weaponRepo;

    public TestMarketInitializer(MarketRepo marketRepo, WeaponRepo weaponRepo) {
        this.marketRepo = marketRepo;
        this.weaponRepo = weaponRepo;
    }

    @Override
    public void run(String... args) {
        // Создание оружия
        Weapon axe = new Weapon();
        axe.setDownDamage(5);
        axe.setUpDamage(10);
        axe.setDistance(1);
        axe.setCost(100);
        axe = weaponRepo.save(axe);

        Weapon spear = new Weapon();
        spear.setDownDamage(3);
        spear.setUpDamage(8);
        spear.setDistance(2);
        spear.setCost(120);
        spear = weaponRepo.save(spear);

        Weapon crossbow = new Weapon();
        crossbow.setDownDamage(4);
        crossbow.setUpDamage(7);
        crossbow.setDistance(3);
        crossbow.setCost(150);
        crossbow = weaponRepo.save(crossbow);

        // Создание рынка
        Market market = new Market();
        market.setName("Рынок у заставы");
        market.setDescription("Здесь продают надёжное оружие для ближнего и дальнего боя");
        market.setWeapons(Set.of(axe.getId(), spear.getId(), crossbow.getId()));
        marketRepo.save(market);
    }
}
