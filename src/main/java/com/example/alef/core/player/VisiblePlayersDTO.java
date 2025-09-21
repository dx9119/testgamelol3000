package com.example.alef.core.player;

import java.util.Set;
import java.util.stream.Collectors;

public class VisiblePlayersDTO {
    private final Set<OtherPlayerDTO> visiblePlayers;

    public VisiblePlayersDTO(Set<OtherPlayerDTO> visiblePlayers) {
        this.visiblePlayers = visiblePlayers;
    }

    public Set<OtherPlayerDTO> getVisiblePlayers() {
        return visiblePlayers;
    }

    public static VisiblePlayersDTO from(Player observer, Set<Player> visiblePlayersSet) {
        Set<OtherPlayerDTO> visibleDTOs = visiblePlayersSet.stream()
                .map(OtherPlayerDTO::from)
                .collect(Collectors.toSet()); // ✅ теперь это Set

        return new VisiblePlayersDTO(visibleDTOs);
    }

    @Override
    public String toString() {
        return "VisiblePlayersDTO{" +
                "visiblePlayers=" + visiblePlayers +
                '}';
    }
}
