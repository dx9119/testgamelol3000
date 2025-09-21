class Player {
    constructor(scene, world) {
        this.scene = scene;
        this.world = world;
        this.x = 0;
        this.y = 0;
        this.target = null;

        this.sprite = scene.add.circle(0, 0, CELL_SIZE / 2 - 4, 0x0077ff);
        this.updatePosition();
    }

    updatePosition() {
        this.sprite.x = this.x * CELL_SIZE + CELL_SIZE / 2;
        this.sprite.y = this.y * CELL_SIZE + CELL_SIZE / 2;
        this.world.loadAround(this.x, this.y);
    }

    setTarget(targetX, targetY) {
        this.target = { x: targetX, y: targetY };
    }

    moveStep() {
        if (!this.target) return;

        const dx = this.target.x - this.x;
        const dy = this.target.y - this.y;

        if (dx === 0 && dy === 0) {
            this.target = null;
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            this.x += Math.sign(dx);
        } else {
            this.y += Math.sign(dy);
        }

        this.updatePosition();
    }
}
