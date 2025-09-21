/* Функция для построения маршрута от текущей позиции к целевой */
function buildPath(currentX, currentY, targetX, targetY) {
    const path = [];
    let x = Math.round(currentX);
    let y = Math.round(currentY);
    const tx = Math.round(targetX);
    const ty = Math.round(targetY);
    while (x < tx) {
        path.push('R');
        x++;
    }
    while (x > tx) {
        path.push('L');
        x--;
    }
    while (y < ty) {
        path.push('D');
        y++;
    }
    while (y > ty) {
        path.push('U');
        y--;
    }
    console.log('Построен маршрут:', path, 'от', { currentX, currentY }, 'к', { targetX, targetY });
    return path;
}

/* Функция для сброса движения */
function resetMovement() {
    currentPath = [];
    isMoving = false;
    console.log('Движение сброшено');
}

/* Функция для выполнения шагов по маршруту */
let isMoving = false;
async function moveAlongPath(path) {
    if (isMoving) return;
    isMoving = true;
    for (let i = 0; i < path.length; i++) {
        if (!isMoving) break;
        const direction = path[i];
        try {
            console.log('Отправка команды /move:', direction, 'Шаг', i + 1, 'из', path.length);
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), 5000); // Таймаут 5 секунд
            const res = await fetch(`/move?stepDirection=${direction}`, {
                method: 'POST',
                headers: {
                    'X-XSRF-TOKEN': getCsrfToken()
                },
                signal: controller.signal
            });
            clearTimeout(timeoutId);
            if (!res.ok) {
                throw new Error(`Ошибка HTTP: ${res.status}`);
            }
            const success = await res.json();
            console.log('Результат /move:', success);
            if (success === true) {
                const position = await getPlayerPositionWithRetry(5, 200);
                if (position) {
                    playerSprite.x = position.x * CELL_SIZE + CELL_SIZE / 2;
                    playerSprite.y = position.y * CELL_SIZE + CELL_SIZE / 2;
                    console.log('Новая позиция игрока из /position:', position);
                    currentPath.shift();
                } else {
                    console.error('Ошибка: не удалось получить позицию после /move');
                    isMoving = false;
                    currentPath = [];
                    return;
                }
            } else {
                console.error('Ошибка: команда /move не выполнена:', success);
                isMoving = false;
                currentPath = [];
                return;
            }
            const expectedX = Math.floor(playerSprite.x / CELL_SIZE);
            const expectedY = Math.floor(playerSprite.y / CELL_SIZE);
            console.log('Текущая позиция игрока:', { expectedX, expectedY });
            // Задержка для синхронизации скорости с сервером (основана на скорости игрока)
            await new Promise(resolve => setTimeout(resolve, 200)); // 200 мс на шаг (speed = 5)
            await updateFromBackend();
        } catch (error) {
            console.error('Ошибка в moveAlongPath:', error);
            isMoving = false;
            currentPath = [];
            return;
        }
    }
    isMoving = false;
    currentPath = [];
}