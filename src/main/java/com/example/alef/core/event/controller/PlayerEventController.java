package com.example.alef.core.event.controller;

import com.example.alef.config.ws.HelloUserMessage;
import com.example.alef.core.command.CommandQueue;
import com.example.alef.core.event.move.MoveCommand;
import com.example.alef.core.event.move.MoveRequestDTO;
import com.example.alef.core.event.move.MoveService;
import com.example.alef.core.player.*;
import com.example.alef.core.location.PositionDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Controller
public class PlayerEventController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PlayerSessionService playerSessionService;
    private final CommandQueue commandQueue;
    private final PlayerRepo playerRepo;
    private final MoveService moveService;
    private final PlayerStateRegistry playerStateRegistry;

    public PlayerEventController(SimpMessagingTemplate messagingTemplate, PlayerSessionService playerSessionService, CommandQueue commandQueue, PlayerRepo playerRepo, MoveService moveService, PlayerStateRegistry playerStateRegistry) {
        this.messagingTemplate = messagingTemplate;
        this.playerSessionService = playerSessionService;
        this.commandQueue = commandQueue;
        this.playerRepo = playerRepo;
        this.moveService = moveService;
        this.playerStateRegistry = playerStateRegistry;
    }

    @MessageMapping("/player/move")
    public void movePlayer(Principal principal, @Payload MoveRequestDTO request) {
        String username = principal.getName();
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.setMoveTo(request.getDirection());

        Optional<Player> optionalPlayer = playerRepo.findByNameAndOnlineTrue(username);
        if (optionalPlayer.isPresent()){
            Player player = optionalPlayer.get();
            Player updatedPlayer = playerRepo.save(moveService.processMove(player, moveCommand));

            PositionDTO currentPlayerPosition = PositionDTO.from(updatedPlayer.getPosition());
            messagingTemplate.convertAndSendToUser(player.getName(), "/queue/events", currentPlayerPosition);
        }

    }

    @MessageMapping("/player/init")
    public void initPlayer(Principal principal) {
        String username = principal.getName();
        Player player = playerRepo.findByNameAndOnlineTrue(username).get();
        playerStateRegistry.register(username, player);

        System.out.println("initPlayer called, principal: " + principal);
        System.out.println("Principal name: " + principal.getName());

        PlayerDTO playerDTO = PlayerDTO.from(player);
        HelloUserMessage msg = new HelloUserMessage(username);
        msg.setPlayerPosition(player.getPosition());
        messagingTemplate.convertAndSendToUser(player.getName(), "/queue/events", playerDTO);
    }


}

