import * as Phaser from '/phaser.esm.js';
import { initWebSocket } from './websocket.js';

let externalUpdatePlayerPosition = null;

export function createGame(containerId, userName) {
  let playerRect = null;
  let graphics = null;
  const otherSprites = new Map(); // key: player.id → Phaser.GameObjects.Circle

  let sceneRef = null;

  const config = {
    type: Phaser.AUTO,
    width: 11 * 50,
    height: 11 * 50,
    parent: containerId,
    backgroundColor: '#111',
    scene: {
      preload() {},
      create: function () {
        sceneRef = this;

        graphics = this.add.graphics();
        drawGrid();

        playerRect = this.add.rectangle(0, 0, 50, 50, 0x00ff00).setOrigin(0, 0);

        initWebSocket({
          scene: this,
          userName,
          onPositionUpdate: updatePlayerPosition,
          onVisibilityUpdate: updateVisiblePlayers,
          onLog: msg => console.log(msg)
        });
      },
      update() {}
    }
  };

  const game = new Phaser.Game(config);

  function drawGrid() {
    graphics.clear();
    graphics.lineStyle(1, 0x333333);
    for (let i = 0; i <= 11; i++) {
      graphics.moveTo(i * 50, 0);
      graphics.lineTo(i * 50, 11 * 50);
      graphics.moveTo(0, i * 50);
      graphics.lineTo(11 * 50, i * 50);
    }
    graphics.strokePath();
  }

  function updatePlayerPosition(pos) {
    if (playerRect) {
      playerRect.x = pos.x * 50;
      playerRect.y = pos.y * 50;
    }
  }

  externalUpdatePlayerPosition = updatePlayerPosition;

  function updateVisiblePlayers(players) {
    if (!sceneRef) return;

    const currentIds = new Set();

    players.forEach(p => {
      if (!p || typeof p !== 'object') return;
      const { id, name, x, y, isMoving, moving } = p;
      if (id == null || name == null || typeof x !== 'number' || typeof y !== 'number') return;
      if (name === userName) return;

      currentIds.add(id);

      let sprite = otherSprites.get(id);
      if (!sprite) {
        sprite = sceneRef.add.circle(x * 50 + 25, y * 50 + 25, 10, 0x00ff00);
        otherSprites.set(id, sprite);
        console.log(`[VISIBILITY] ➕ Добавлен игрок ${name} (${id})`);
      } else {
        sprite.setPosition(x * 50 + 25, y * 50 + 25);
      }

      const isActuallyMoving = isMoving ?? moving ?? false;
      sprite.setFillStyle(isActuallyMoving ? 0xff0000 : 0x00ff00);
    });

    for (const [id, sprite] of otherSprites.entries()) {
      if (!currentIds.has(id)) {
        sprite.destroy();
        otherSprites.delete(id);
        console.log(`[VISIBILITY] ➖ Удалён игрок ${id}`);
      }
    }
  }

  return {
    game
  };
}

export { externalUpdatePlayerPosition as updatePlayerPosition };
