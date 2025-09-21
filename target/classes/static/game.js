/* Размер одной клетки в пикселях */
const CELL_SIZE = 50;

/* Переменные для хранения объектов Phaser */
let playerSprite; // Спрайт игрока
let graphics;     // Объект для рисования графики (сетки и пути)
let sceneRef;     // Ссылка на текущую сцену Phaser
let coordText;    // Текстовый объект для отображения координат

/* Конфигурация игры Phaser */
const config = {
    type: Phaser.AUTO,
    parent: 'game-container',
    width: window.innerWidth,
    height: window.innerHeight,
    backgroundColor: '#eeeeee',
    physics: {
        default: 'arcade',
        arcade: { debug: false }
    },
    scene: {
        create() {
            sceneRef = this;

            // Проверяем начальные координаты игрока
            console.log('Начальная позиция игрока:', { startX, startY });

            // Создаем игрока (прямоугольник синего цвета)
            playerSprite = this.add.rectangle(0, 0, CELL_SIZE * 0.8, CELL_SIZE * 0.8, 0x0000ff);
            this.physics.add.existing(playerSprite);
            playerSprite.body.setImmovable(true);

            // Устанавливаем начальную позицию игрока
            if (typeof startX === 'number' && typeof startY === 'number') {
                playerSprite.x = startX * CELL_SIZE + CELL_SIZE / 2;
                playerSprite.y = startY * CELL_SIZE + CELL_SIZE / 2;
            } else {
                console.error('Ошибка: некорректные startX или startY:', { startX, startY });
                playerSprite.x = CELL_SIZE / 2;
                playerSprite.y = CELL_SIZE / 2;
            }

            // Устанавливаем уровень отображения (depth) для игрока
            playerSprite.setDepth(1);
            playerSprite.visibleCell = false;

            // Настраиваем камеру
            this.cameras.main.startFollow(playerSprite, true, 0.1, 0.1);

            // Создаем объект для рисования графики
            graphics = this.add.graphics();

            // Создаем текстовый объект для координат в правом нижнем углу
            coordText = this.add.text(
                window.innerWidth - 100, // Отступ от правого края
                window.innerHeight - 30, // Отступ от нижнего края
                '(0, 0)', // Начальный текст
                {
                    fontSize: '20px',
                    color: '#000000',
                    backgroundColor: '#ffffff',
                    padding: { x: 5, y: 5 }
                }
            );
            coordText.setDepth(2); // Выше игрока и клеток
            coordText.setScrollFactor(0); // Фиксируем текст на экране (не движется с камерой)

            // Подключаем обработчики
            this.input.on('pointerdown', (pointer) => handleClick(pointer));
            this.input.on('pointermove', (pointer) => handlePointerMove(pointer));
            this.input.keyboard.on('keydown-ESC', () => resetMovement());

            // Запускаем обновление данных
            setInterval(updateFromBackend, 500);
        },
        update() {
            drawGrid(sceneRef);
            drawPath(sceneRef);
        }
    }
};

/* Создаем экземпляр игры */
const game = new Phaser.Game(config);

/* Обработчик изменения размера окна */
window.addEventListener('resize', () => {
    game.scale.resize(window.innerWidth, window.innerHeight);
    game.cameras.main.setSize(window.innerWidth, window.innerHeight);
    // Перемещаем текстовый объект в правый нижний угол
    if (coordText) {
        coordText.setPosition(window.innerWidth - 100, window.innerHeight - 30);
    }
});