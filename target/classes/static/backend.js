/* Функция для получения текущей позиции игрока с сервера */
async function getPlayerPosition() {
    try {
        const res = await fetch('/position');
        if (!res.ok) {
            throw new Error(`Ошибка HTTP: ${res.status}`);
        }
        const position = await res.json();
        if (position && typeof position.x === 'number' && typeof position.y === 'number') {
            return position;
        } else {
            throw new Error('Некорректная позиция игрока:', position);
        }
    } catch (error) {
        console.error('Ошибка при получении позиции игрока:', error);
        return null;
    }
}

/* Функция для получения позиции с повторными попытками */
async function getPlayerPositionWithRetry(maxRetries, delayMs) {
    let retries = 0;
    while (retries < maxRetries) {
        const position = await getPlayerPosition();
        if (position) {
            return position;
        }
        retries++;
        console.log('Повторная попытка получения позиции, попытка', retries);
        await new Promise(resolve => setTimeout(resolve, delayMs));
    }
    console.error('Не удалось получить позицию после', maxRetries, 'попыток');
    return null;
}

/* Функция для обновления игровых объектов с бэкенда */
function updateFromBackend() {
    fetch('/watch')
        .then(res => {
            if (!res.ok) throw new Error(`Ошибка HTTP: ${res.status}`);
            return res.json();
        })
        .then(cells => {
            console.log('Данные от сервера (/watch):', cells);
            if (!Array.isArray(cells)) {
                console.error('Ошибка: cells не является массивом:', cells);
                return;
            }
            sceneRef.children.list
                .filter(obj => obj.visibleCell)
                .forEach(obj => obj.destroy());
            cells.forEach(cell => {
                if (!cell || typeof cell.x !== 'number' || typeof cell.y !== 'number') {
                    console.error('Ошибка: некорректный формат клетки:', cell);
                    return;
                }
                const x = cell.x * CELL_SIZE + CELL_SIZE / 2;
                const y = cell.y * CELL_SIZE + CELL_SIZE / 2;
                const rect = sceneRef.add.rectangle(x, y, CELL_SIZE * 0.9, CELL_SIZE * 0.9, 0xaaaaaa);
                rect.visibleCell = true;
                rect.setDepth(0);
            });
            getPlayerPosition().then(position => {
                if (position) {
                    const newX = position.x * CELL_SIZE + CELL_SIZE / 2;
                    const newY = position.y * CELL_SIZE + CELL_SIZE / 2;
                    if (Math.abs(playerSprite.x - newX) > 0.1 || Math.abs(playerSprite.y - newY) > 0.1) {
                        console.log('Синхронизация позиции игрока из /position:', position);
                        playerSprite.x = newX;
                        playerSprite.y = newY;
                    }
                }
            });
        })
        .catch(error => console.error('Ошибка в updateFromBackend:', error));
}