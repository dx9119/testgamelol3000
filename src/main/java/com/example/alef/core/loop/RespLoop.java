package com.example.alef.core.loop;

import com.example.alef.core.enemy.Enemy;
import com.example.alef.core.enemy.EnemyRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
public class RespLoop {

    private final EnemyRepo enemyRepo;

    public RespLoop(EnemyRepo enemyRepo) {
        this.enemyRepo = enemyRepo;
    }

    @Scheduled(fixedRate = 300000) // 5 минут = 5 * 60 * 1000 = 300_000 мс
    public void respawnEnemy(){
        respEnemy();
    }

    private void respEnemy(){
        Set<Enemy> enemies = enemyRepo.findAllByLiveFalseAndTimeOfRespBefore(Instant.now());
        //удалить старое
        for (Enemy enemy : enemies){
            enemyRepo.delete(enemy);
        }
        //создать новое
        // через генерацию мира
    }
}
