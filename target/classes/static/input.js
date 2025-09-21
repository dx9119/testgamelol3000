function setupInput(scene, player) {
    scene.input.on('pointerdown', (pointer) => {
        const worldX = Math.floor(pointer.worldX / CELL_SIZE);
        const worldY = Math.floor(pointer.worldY / CELL_SIZE);
        player.setTarget(worldX, worldY);
    });
}
