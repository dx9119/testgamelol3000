package com.example.alef.core.market;

import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import com.example.alef.core.player.Weapon;
import com.example.alef.core.player.WeaponRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/market")
public class MarketController {

    private final MarketService marketService;
    private final PlayerRepo playerRepo;
    private final MarketRepo marketRepo;
    private final WeaponRepo weaponRepo;

    public MarketController(MarketService marketService, PlayerRepo playerRepo, MarketRepo marketRepo, WeaponRepo weaponRepo) {
        this.marketService = marketService;
        this.playerRepo = playerRepo;
        this.marketRepo = marketRepo;
        this.weaponRepo = weaponRepo;
    }

    // Главная страница магазина
    @GetMapping("/")
    public String showMarket(@RequestParam Long marketId,
                             Principal principal,
                             Model model) throws Exception {

        Player player = playerRepo.findByName(principal.getName())
                .orElseThrow(() -> new Exception("Игрок не найден"));

        Market market = marketRepo.findById(marketId)
                .orElseThrow(() -> new Exception("Рынок не найден"));

        Set<Long> weaponIds = market.getWeapons();
        List<Weapon> weapons = weaponIds.stream()
                .map(id -> weaponRepo.findById(id).orElse(null))
                .filter(w -> w != null)
                .collect(Collectors.toList());

        model.addAttribute("player", player);
        model.addAttribute("market", market);
        model.addAttribute("weapons", weapons);

        return "marketMain"; // шаблон главной страницы
    }

    // Покупка оружия
    @PostMapping("/buy")
    @Transactional
    public String buyWeapon(@RequestParam Long marketId,
                            @RequestParam Long weaponId,
                            Principal principal,
                            Model model) throws Exception {

        Player player = playerRepo.findByName(principal.getName())
                .orElseThrow(() -> new Exception("Игрок не найден"));

        Market market = marketRepo.findById(marketId)
                .orElseThrow(() -> new Exception("Рынок не найден"));

        BuyDTO buyDTO = marketService.buyWeapon(market, player, weaponId);
        model.addAttribute("buyResult", buyDTO);

        return "buyResult";
    }

    // Продажа оружия
    @PostMapping("/sell")
    @Transactional
    public String sellWeapon(@RequestParam Long marketId,
                             @RequestParam Long weaponId,
                             Principal principal,
                             Model model) throws Exception {

        Player player = playerRepo.findByName(principal.getName())
                .orElseThrow(() -> new Exception("Игрок не найден"));

        Market market = marketRepo.findById(marketId)
                .orElseThrow(() -> new Exception("Рынок не найден"));

        SellInMarketDTO sellDTO = marketService.sellWeapon(market, player, weaponId);
        model.addAttribute("sellResult", sellDTO);

        return "sellResult";
    }
}
