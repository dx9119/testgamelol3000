package com.example.alef.core.player;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerStateRegistry {

    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    public void setPlayers(ConcurrentHashMap<String, Player> players) {
        this.players = players;
    }

    public void register(String name, Player player) {
        players.put(name, player);
    }

    public void unregister(String name) {
        players.remove(name);
    }

    public Player getPlayerFromName(String name) {
        return players.get(name);
    }

}
