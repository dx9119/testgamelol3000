package com.example.alef.core.inventory;

import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class InventoryController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PlayerRepo playerRepo;

    public InventoryController(SimpMessagingTemplate messagingTemplate, PlayerRepo playerRepo) {
        this.messagingTemplate = messagingTemplate;
        this.playerRepo = playerRepo;
    }

    @MessageMapping("/inventory/sell") // клиент отправляет на /app/inventory/sell
    public void handleSellCommand(Principal principal) {
        String username = principal.getName();
        Player player = playerRepo.findByNameAndOnlineTrue(username).orElse(null);

        if (player == null) {
            messagingTemplate.convertAndSendToUser(username, "/topic/inventory/sellResult", "Игрок не найден");
            return;
        }

        int inventoryCost=0;
        List<PlayerItem> items = player.getInventory();

        if(items.size() == 0){
            messagingTemplate.convertAndSendToUser(username, "/topic/inventory/sellResult", "Нечего продавать");
            return;
        }

        for (PlayerItem item : items){
            inventoryCost = inventoryCost + item.getItem().getCost() *item.getCount();
        }
        player.setBibaCoinsCount(player.getBibaCoinsCount()+inventoryCost);
        player.setInventory(new ArrayList<>());
        playerRepo.save(player);
        messagingTemplate.convertAndSendToUser(username, "/topic/inventory/sellResult", "Вы получили:"+new SellDTO(inventoryCost));
    }

}


