package com.example.alef.core.player;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "player_level")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "level_number", nullable = false)
    private int levelNumber = 1;

    @Column(name = "experience", nullable = false)
    private int experience = 1;

    @Transient
    private static final Map<Integer, Integer> experienceTable = generateExperienceTable();

    @OneToOne(mappedBy = "level")
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean canLevelUp() {
        return experience >= getExperienceToNext();
    }

    public void levelUp() {
        if (canLevelUp() && levelNumber < 500) {
            experience -= getExperienceToNext();
            levelNumber++;
        }
    }

    public int getExperienceToNext() {
        return experienceTable.getOrDefault(levelNumber, Integer.MAX_VALUE);
    }

    private static Map<Integer, Integer> generateExperienceTable() {
        Map<Integer, Integer> table = new HashMap<>();
        int base = 500;

        for (int i = 1; i <= 500; i++) {
            int exp = (int) (base * Math.log(i + 1)); // логарифм от (level + 1)
            table.put(i, exp);
        }

        return table;
    }

}
