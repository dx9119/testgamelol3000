/* Массив для хранения текущего пути (для отрисовки) */
let currentPath = [];
let targetX, targetY; // Целевая клетка для отладки

/* Функция для рисования сетки на экране */
function drawGrid(scene) {
    graphics.clear();
    graphics.lineStyle(1, 0x999999, 1);
    const cam = scene.cameras.main;
    const left = cam.scrollX;
    const top = cam.scrollY;
    const right = cam.scrollX + cam.width;
    const bottom = cam.scrollY + cam.height;
    const startX = Math.floor(left / CELL_SIZE) - 1;
    const endX = Math.floor(right / CELL_SIZE) + 1;
    const startY = Math.floor(top / CELL_SIZE) - 1;
    const endY = Math.floor(bottom / CELL_SIZE) + 1;
    for (let x = startX; x <= endX; x++) {
        graphics.moveTo(x * CELL_SIZE, top - CELL_SIZE);
        graphics.lineTo(x * CELL_SIZE, bottom + CELL_SIZE);
    }
    for (let y = startY; y <= endY; y++) {
        graphics.moveTo(left - CELL_SIZE, y * CELL_SIZE);
        graphics.lineTo(right + CELL_SIZE, y * CELL_SIZE);
    }
    graphics.strokePath();
}

/* Функция для рисования большой зеленой стрелки, указывающей направление к цели */
function drawPath(scene) {
    if (typeof targetX !== 'number' || typeof targetY !== 'number') return;

    graphics.lineStyle(3, 0x00ff00, 1); // Тоньше и мягче

    const currentX = Math.floor(playerSprite.x / CELL_SIZE);
    const currentY = Math.floor(playerSprite.y / CELL_SIZE);

    const startX = currentX * CELL_SIZE + CELL_SIZE / 2;
    const startY = currentY * CELL_SIZE + CELL_SIZE / 2;

    const dx = targetX - currentX;
    const dy = targetY - currentY;
    const angle = Math.atan2(dy, dx);

    const arrowLength = CELL_SIZE * 1.5; // Меньше: 1.5 клетки
    const endX = startX + Math.cos(angle) * arrowLength;
    const endY = startY + Math.sin(angle) * arrowLength;

    // Основная линия
    graphics.beginPath();
    graphics.moveTo(startX, startY);
    graphics.lineTo(endX, endY);

    // Наконечник: компактный треугольник
    const headSize = CELL_SIZE * 0.3;
    const leftX = endX - headSize * Math.cos(angle - Math.PI / 6);
    const leftY = endY - headSize * Math.sin(angle - Math.PI / 6);
    const rightX = endX - headSize * Math.cos(angle + Math.PI / 6);
    const rightY = endY - headSize * Math.sin(angle + Math.PI / 6);

    graphics.moveTo(endX, endY);
    graphics.lineTo(leftX, leftY);
    graphics.moveTo(endX, endY);
    graphics.lineTo(rightX, rightY);

    graphics.strokePath();
}
