package com.example.alef.core.player;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PlayerDetails implements UserDetails {

    private Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerDetails(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // или роли, если нужны
    }

    @Override
    public String getPassword() {
        return "1234"; // или player.getPassword(), если добавишь поле
    }

    @Override
    public String getUsername() {
        return player.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

