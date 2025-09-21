package com.example.alef.core.event.attack;

import com.example.alef.core.location.Position;
import com.example.alef.core.player.Player;
import com.example.alef.core.player.PlayerRepo;
import com.example.alef.core.player.PlayerStateRegistry;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class AttackController {

    private final AttackService attackService;
    private final PlayerStateRegistry playerStateRegistry;
    private final CombatNotifier combatNotifier;

    private final PlayerRepo playerRepo;

    public AttackController(AttackService attackService, PlayerStateRegistry playerStateRegistry, CombatNotifier combatNotifier,
                            PlayerRepo playerRepo) {
        this.attackService = attackService;
        this.playerStateRegistry = playerStateRegistry;
        this.combatNotifier = combatNotifier;
        this.playerRepo = playerRepo;
    }

    @MessageMapping("/player/attack")
    public void handleAttack(Principal principal, @Payload AttackRequestDTO request) {
        try {
            String username = principal.getName();
            Player attacker = playerRepo.findByName(username).get();
            Position targetPosition = new Position(request.getTargetX(), request.getTargetY());

            Object result = attackService.processAttack(attacker, targetPosition);
            if (result instanceof CombatSuccessDTO) {
                combatNotifier.notifyPlayers(result);
                CombatSuccessDTO successDto = (CombatSuccessDTO) result;

                System.out.printf("[COMBAT] %s attacked targets at (%d,%d):%n",
                        successDto.getAttackerName(),
                        targetPosition.getX(),
                        targetPosition.getY());

                for (CombatSuccessDTO.TargetHit hit : successDto.getHits()) {
                    System.out.printf("  → %s took %d damage. Remaining HP: %d%n",
                            hit.getTargetName(),
                            hit.getDamage(),
                            hit.getRemainingHP());
                }
            }

        } catch (Exception e) {
            System.err.println("[ATTACK] Error processing attack: " + e.getMessage());
            e.printStackTrace();
        }
    }
}