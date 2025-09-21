package com.example.alef.config.ws;

import com.example.alef.core.location.Position;
import com.example.alef.core.player.PlayerStateRegistry;
import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import com.example.alef.core.player.PlayerSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private PlayerStateRegistry playerStateRegistry;
    @Autowired
    private PlayerSessionService playerSessionService;
    @Autowired
    private PlayerRepo playerRepo;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getUser().getName();

        System.out.println("WebSocket connected for user: " + username + ", name: " + username);

        Optional<Player> oPplayer = playerRepo.findByName(username);
        if(oPplayer.isPresent()){
            Player player = oPplayer.get();
            playerStateRegistry.register(username, player);
            playerSessionService.markOnline(player);
            HelloUserMessage helloUserMessage = new HelloUserMessage(username);
            helloUserMessage.setPlayerPosition(player.getPosition());
        }

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : null;

        if (username != null) {
            System.out.println("WebSocket disconnected for user: " + username);

            // Получаем ID отключающегося игрока перед удалением
            Player disconnectedPlayer = playerStateRegistry.getPlayerFromName(username);
            Long disconnectedPlayerId = disconnectedPlayer != null ? disconnectedPlayer.getId() : null;

            // Удаление игрока из реестра
            playerStateRegistry.unregister(username);
            playerSessionService.markOffline(username);
            // Отправляем BROADCAST сообщение всем игрокам об отключении
            if (disconnectedPlayerId != null) {
                sendPlayerDisconnectBroadcast(disconnectedPlayerId);
            }
        }
    }

    private void sendPlayerDisconnectBroadcast(Long playerId) {
        // Создаем сообщение об отключении
        Map<String, Object> disconnectMessage = new HashMap<>();
        disconnectMessage.put("type", "playerDisconnect");
        disconnectMessage.put("playerId", playerId);
        disconnectMessage.put("timestamp", System.currentTimeMillis());

        // Отправляем BROADCAST всем подписанным клиентам
        try {
            messagingTemplate.convertAndSend(
                    "/topic/player/disconnect", // BROADCAST endpoint
                    disconnectMessage
            );
            System.out.println("Broadcast disconnect message for player: " + playerId);
        } catch (Exception e) {
            System.err.println("Failed to broadcast disconnect message: " + e.getMessage());
        }
    }

}