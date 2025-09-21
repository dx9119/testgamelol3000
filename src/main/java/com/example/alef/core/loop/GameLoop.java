package com.example.alef.core.loop;

import com.example.alef.core.command.CommandQueue;
import com.example.alef.core.enemy.*;
import com.example.alef.core.location.*;
import com.example.alef.core.event.attack.AttackService;
import com.example.alef.core.player.*;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GameLoop {

    private final CommandQueue commandQueue;
    private final PlayerStateRegistry playerStateRegistry;
    private final SimpMessagingTemplate messagingTemplate;
    private final AttackService attackService;
    private final CellRepo cellRepo;
    private final EnemyRepo enemyRepo;
    private final EnemyMoveService enemyMoveService;
    private final EnemyAIService enemyAIService;
    private final WeaponRepo weaponRepo;

    private final PlayerRepo playerRepo;

    //счетчик тиков
    public static final AtomicLong gameTick = new AtomicLong(0);


    public GameLoop(CommandQueue commandQueue,
                    PlayerStateRegistry playerStateRegistry,
                    SimpMessagingTemplate messagingTemplate,
                    AttackService attackService,
                    CellRepo cellRepo,
                    EnemyRepo enemyRepo,
                    EnemyMoveService enemyMoveService,
                    EnemyAIService enemyAIService, WeaponRepo weaponRepo,
                    PlayerRepo playerRepo) {
        this.commandQueue = commandQueue;
        this.playerStateRegistry = playerStateRegistry;
        this.messagingTemplate = messagingTemplate;
        this.attackService = attackService;
        this.cellRepo = cellRepo;
        this.enemyRepo = enemyRepo;
        this.enemyMoveService = enemyMoveService;
        this.enemyAIService = enemyAIService;
        this.weaponRepo = weaponRepo;
        this.playerRepo = playerRepo;
    }


    // Главный игровой цикл — вызывается каждые * мс
    @Scheduled(fixedRate = 200)
    public void gameLoop() {
        gameTick.incrementAndGet();
        updatePlayerEnvironment();    // Обновление видимых клеток и врагов
        updatePlayerVisibility();     // Обновление видимости игроков
        updateWeaponsEnergy();  // Обновление готовности оружия
    }

    // Обновление готовности оружия стрелять
    @Transactional
    private void updateWeaponsEnergy(){
        Set<Weapon> weapons = weaponRepo.findAllByAvailableEnergy(0);
        for(Weapon weapon:weapons){
            weapon.setAvailableEnergy(weapon.getAvailableEnergy()+1);
        }
    }

    // Получить "свежих" игроков
    private Set<Player> getActivePlayers(){
        Set<Player> players = playerRepo.findAllByOnlineTrue();
        return players;
    }

    // Обновление видимости других игроков и врагов
    @Transactional
    private void updatePlayerVisibility() {

        for (Player player : getActivePlayers()){
            Set<Position> playersPos = getPositionVisibleOfPlayer(player, TypeVision.PLAYER);

            Set<OtherPlayerDTO> allOtherPlayers = calculateVisiblePlayers(playersPos);
            if(!allOtherPlayers.isEmpty()){
                messagingTemplate.convertAndSendToUser(player.getName(), "/queue/visibility", allOtherPlayers);
            }

            // Для активации врагов
            Set<Position> enemyPos = getPositionVisibleOfPlayer(player, TypeVision.ENEMY);
            Set<Enemy> enemies = calculateVisibleEnemy(enemyPos);
            // Для оповещения игрока
            Set<EnemyDTO> allEnemyNear = new HashSet<>();
            for (Enemy enemy:enemies){
                allEnemyNear.add(EnemyDTO.from(enemy));
            }
            if(!allEnemyNear.isEmpty()){
                messagingTemplate.convertAndSendToUser(player.getName(), "/queue/visibility", allEnemyNear);
            }

            if (!enemies.isEmpty()){
                for (Enemy enemy : enemies){
                    enemyActivate(enemy,player);
                }
            }
        }
    }

    // Обновление игрового окружения
    @Transactional
    private void updatePlayerEnvironment() {
        for (Player player : getActivePlayers()){
            Set<Position> positions = getPositionVisibleOfPlayer(player,TypeVision.OBJECT);
            Set<CellDTO> allGameObj = calculateCellWithGameObj(positions);
            if(!allGameObj.isEmpty()){
                messagingTemplate.convertAndSendToUser(player.getName(), "/queue/environment", allGameObj);
            }
        }
    }

    // Вычисление игровых клеток с не живыми объектами
    private Set<CellDTO> calculateCellWithGameObj(Set<Position> positions){
        Set<CellDTO> cellDTOs = new HashSet<>();
        for (Position p : positions){
            Optional<Cell> maybeCell = cellRepo.findByPositionXAndPositionY(p.getX(),p.getY());
            maybeCell.ifPresentOrElse(
                    cell -> {
                        cellDTOs.add(CellDTO.from(cell));
                    },
                    () -> {
                        return;
                    }
            );
        }
        return cellDTOs;
    }

    // Вычисление видимых игроков для одного наблюдателя
    private Set<OtherPlayerDTO> calculateVisiblePlayers(Set<Position> positions) {
        Set<OtherPlayerDTO> otherPlayerDTO = new HashSet<>();
        for (Position p : positions){
            Set<Player> otherPlayers = playerRepo.findAllByPositionAndOnlineTrue(p);
            for (Player pl : otherPlayers){
                otherPlayerDTO.add(OtherPlayerDTO.from(pl));
            }
        }
        return otherPlayerDTO;
    }

    // Вычисление видимых игроком врагов
    private Set<Enemy> calculateVisibleEnemy(Set<Position> positions){
        Set<Enemy> enemies = new HashSet<>();
        for (Position p: positions){
            enemies.addAll(enemyRepo.findByPositionXAndPositionY(p.getX(),p.getY()));
        }
        return enemies;
    }

    // Заставляем врага напасть на игрока
    @Transactional
    private void enemyActivate(Enemy enemy, Player player){
        enemyAIService.markPlayerAsVisible(enemy,player);
    }

    private Set<Position> getPositionVisibleOfPlayer(Player observer, TypeVision typeVision) {
        Set<Position> visiblePos = new HashSet<>();

        int visionRadius;

        switch (typeVision) {
            case PLAYER:
                visionRadius = observer.getVision(); // полный радиус
                break;
            case ENEMY:
                visionRadius = observer.getVision(); // пока такой же
                break;
            case OBJECT:
                visionRadius = observer.getVision() / 2; // половина радиуса
                break;
            default:
                visionRadius = 0;
        }

        for (int dx = -visionRadius; dx <= visionRadius; dx++) {
            for (int dy = -visionRadius; dy <= visionRadius; dy++) {
                Position vPos = new Position();
                vPos.setX(observer.getPosition().getX() + dx);
                vPos.setY(observer.getPosition().getY() + dy);
                visiblePos.add(vPos);
            }
        }

        return visiblePos;
    }


}
