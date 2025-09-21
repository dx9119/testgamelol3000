const visiblePlayerSprites = new Map(); // key: player.id → Phaser.Sprite

export function initVisibilityTracking({ stompClient, log, scene, userName, onVisibilityUpdate }) {
    const visibilityDest = `/user/${userName}/queue/visibility`;

    const visibilitySub = stompClient.subscribe(visibilityDest, msg => {
        log(`[RAW] ${msg.body}`);

        let dto;
        try {
            dto = JSON.parse(msg.body);
        } catch (e) {
            log(`[VISIBILITY] ❌ JSON.parse error: ${e.message}`);
            log(`[VISIBILITY] ❌ Невозможно распарсить: ${msg.body}`);
            return;
        }

        if (!dto || typeof dto !== 'object') {
            log(`[VISIBILITY] ❌ DTO не является объектом: ${msg.body}`);
            return;
        }

        if (!Array.isArray(dto.visiblePlayers)) {
            log(`[VISIBILITY] ❌ visiblePlayers не массив: ${JSON.stringify(dto.visiblePlayers)}`);
            return;
        }

        updateVisiblePlayers(dto.visiblePlayers, scene, log, userName);
        log(`[VISIBILITY] ✅ Получено ${dto.visiblePlayers.length} игроков`);

        // 🔔 Передаём наружу, если нужно
        onVisibilityUpdate?.(dto.visiblePlayers);
    });

    return { visibilityDest, visibilitySub };
}

function updateVisiblePlayers(visiblePlayers, scene, log, userName) {
    const currentIds = new Set();

    visiblePlayers.forEach(p => {
        if (!p || typeof p !== 'object') {
            log(`[VISIBILITY] ❌ Игнорируем некорректный объект: ${JSON.stringify(p)}`);
            return;
        }

        const { id, name, x, y, isMoving, moving } = p;

        if (id == null || name == null || typeof x !== 'number' || typeof y !== 'number') {
            log(`[VISIBILITY] ❌ Пропущен некорректный игрок: ${JSON.stringify(p)}`);
            return;
        }

        if (name === userName) {
            log(`[VISIBILITY] 🔍 Пропущен свой игрок: ${name} (${id})`);
            return;
        }

        currentIds.add(id);

        let sprite = visiblePlayerSprites.get(id);
        if (!sprite) {
            sprite = scene.add.circle(x, y, 10, 0x00ff00);
            visiblePlayerSprites.set(id, sprite);
            log(`[VISIBILITY] ➕ Добавлен игрок ${name} (${id})`);
        } else {
            sprite.setPosition(x, y);
        }

        const isActuallyMoving = isMoving ?? moving ?? false;
        sprite.setFillStyle(isActuallyMoving ? 0xff0000 : 0x00ff00);
    });

    for (const [id, sprite] of visiblePlayerSprites.entries()) {
        if (!currentIds.has(id)) {
            sprite.destroy();
            visiblePlayerSprites.delete(id);
            log(`[VISIBILITY] ➖ Удалён игрок ${id}`);
        }
    }
}
