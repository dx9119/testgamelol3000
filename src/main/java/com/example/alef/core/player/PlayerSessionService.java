package com.example.alef.core.player;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class PlayerSessionService {

    private final PlayerRepo playerRepo;

    public PlayerSessionService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    // Получает текущего игрока из SecurityContext (используется в обычных HTTP-контекстах)
    public Player getCurrentPlayer() {
        Authentication auth = getAuthentication(); // Извлекаем Authentication из SecurityContext

        // Проверяем, что principal — это кастомный PlayerDetails
        if (auth.getPrincipal() instanceof PlayerDetails playerDetails) {
            return playerDetails.getPlayer(); // Возвращаем объект Player
        }

        // Если principal не содержит PlayerDetails — выбрасываем исключение
        throw new IllegalStateException("No authenticated player found in session");
    }

    // Получает игрока из переданного Principal (удобно для WebSocket-контроллеров)
    public Player getPlayerFromPrincipal(Principal principal) {
        // Проверяем, что principal — это Authentication, и его principal — это PlayerDetails
        if (principal instanceof Authentication authentication &&
                authentication.getPrincipal() instanceof PlayerDetails playerDetails) {
            return playerDetails.getPlayer(); // Возвращаем объект Player
        }

        // Если не удалось извлечь Player — выбрасываем исключение
        throw new IllegalStateException("Principal does not contain a valid PlayerDetails");
    }

    // Вспомогательный метод для получения Authentication из SecurityContext
    private Authentication getAuthentication() {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, что сессия существует и пользователь аутентифицирован
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated session found");
        }

        return auth; // Возвращаем Authentication
    }

    @Transactional
    public void markOffline(String username){
        Optional<Player> OptiPlayer = playerRepo.findByName(username);
        if(OptiPlayer.isPresent()){
            Player player = OptiPlayer.get();
            player.setOnline(true);
            playerRepo.save(player);
        }

    }

    @Transactional
    public void markOnline(Player player){
        player.setOnline(true);
        playerRepo.save(player);
    }
}
