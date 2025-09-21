package com.example.alef.core.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface WeaponRepo extends JpaRepository<Weapon, Long> {
    Set<Weapon> findAllByAvailableEnergy(Integer integer);
}
