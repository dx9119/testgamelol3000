package com.example.alef.core.event.attack;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class CombatNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public CombatNotifier(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyPlayers(Object result) {
        if (result instanceof CombatSuccessDTO) {
            CombatSuccessDTO successDto = (CombatSuccessDTO) result;
            messagingTemplate.convertAndSendToUser(successDto.getHits().toString(), "/queue/combat", successDto);
            messagingTemplate.convertAndSendToUser(successDto.getAttackerName(), "/queue/combat", successDto);
        } else if (result == null) {
            // Ничего не делаем, так как промах уже отправлен в AttackService
        }
    }
}