package com.example.alef.core.player;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PlayerDetailsService implements UserDetailsService {

    private final PlayerRepo playerRepository;

    public PlayerDetailsService(PlayerRepo playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Player not found: " + username));
        return new PlayerDetails(player);
    }
}

