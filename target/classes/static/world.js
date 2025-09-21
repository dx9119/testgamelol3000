class WorldManager {
    constructor(scene) {
        this.scene = scene;
        this.cells = new Map(); // key: `${x},${y}` → graphics
    }

    loadAround(x, y) {
        const minX = x - LOAD_RADIUS;
        const maxX = x + LOAD_RADIUS;
        const minY = y - LOAD_RADIUS;
        const maxY = y + LOAD_RADIUS;

        const newCells = new Map();

        for (let i = minX; i <= maxX; i++) {
            for (let j = minY; j <= maxY; j++) {
                const key = `${i},${j}`;
                if (!this.cells.has(key)) {
                    const g = this.scene.add.rectangle(
                        i * CELL_SIZE,
                        j * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE,
                        0xcccccc
                    ).setOrigin(0);
                    newCells.set(key, g);
                } else {
                    newCells.set(key, this.cells.get(key));
                }
            }
        }

        // Удаляем старые клетки
        for (const [key, g] of this.cells.entries()) {
            if (!newCells.has(key)) {
                g.destroy();
            }
        }

        this.cells = newCells;
    }
}
