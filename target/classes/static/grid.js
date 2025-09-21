// Текущая позиция игрока (по умолчанию центр поля 11x11)
let currentPosition = { x: 5, y: 5 };
// Список других игроков, видимых на поле
let otherPlayers = [];

// Создает игровое поле 11x11 ячеек
function generateGrid() {
    const grid = document.getElementById('gameGrid'); // Находим контейнер поля
    if (!grid) {
        addLogEntry('Ошибка: Контейнер игрового поля не найден');
        return;
    }
    grid.innerHTML = ''; // Очищаем содержимое поля
    // Цикл по строкам (y) и столбцам (x) для создания ячеек
    for (let y = 0; y < 11; y++) {
        for (let x = 0; x < 11; x++) {
            const cell = document.createElement('div'); // Создаем новую ячейку
            cell.className = 'cell'; // Присваиваем класс для стилизации
            cell.id = `cell-${x}-${y}`; // Уникальный ID ячейки (например, cell-3-4)
            // Добавляем препятствия в фиксированных местах
            if ((x === 3 && y === 3) || (x === 7 && y === 7) || (x === 2 && y === 8)) {
                cell.classList.add('obstacle'); // Помечаем ячейку как препятствие
            }
            grid.appendChild(cell); // Добавляем ячейку в поле
        }
    }
    updatePlayerPosition(); // Обновляем позиции игроков
}

// Обновляет отображение текущего и других игроков на поле
function updatePlayerPosition() {
    // Проверяем, находятся ли координаты в пределах поля (0–10)
    if (currentPosition.x < 0 || currentPosition.x > 10 || currentPosition.y < 0 || currentPosition.y > 10) {
        addLogEntry(`Ошибка: Некорректная позиция игрока X: ${currentPosition.x}, Y: ${currentPosition.y}`);
        return;
    }
    // Очищаем содержимое всех ячеек (удаляем игроков)
    document.querySelectorAll('.cell').forEach(cell => cell.innerHTML = '');
    // Отображаем текущего игрока (зеленый круг)
    const playerCell = document.getElementById(`cell-${currentPosition.x}-${currentPosition.y}`);
    if (playerCell) {
        playerCell.innerHTML = '<div class="player"></div>'; // Добавляем зеленый круг
    } else {
        addLogEntry(`Ошибка: Ячейка для позиции X: ${currentPosition.x}, Y: ${currentPosition.y} не найдена`);
    }
    // Отображаем других игроков (синие круги), исключая текущего
    if (playerName) { // Проверяем, что playerName определено
        otherPlayers.forEach(player => {
            if (player.name && player.name !== playerName) {
                if (player.x >= 0 && player.x <= 10 && player.y >= 0 && player.y <= 10) {
                    const cell = document.getElementById(`cell-${player.x}-${player.y}`);
                    if (cell) {
                        cell.innerHTML = '<div class="other-player"></div>'; // Добавляем синий круг
                    }
                } else {
                    addLogEntry(`Ошибка: Некорректная позиция игрока ${player.name}: X: ${player.x}, Y: ${player.y}`);
                }
            }
        });
    }
    // Обновляем текст с текущей позицией игрока
    document.getElementById('currentPosition').textContent = `X: ${currentPosition.x}, Y: ${currentPosition.y}`;
}