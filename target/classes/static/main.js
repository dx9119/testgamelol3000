const config = {
    type: Phaser.AUTO,
    width: window.innerWidth,
    height: window.innerHeight,
    backgroundColor: '#222',
    scale: {
        mode: Phaser.Scale.RESIZE,
        autoCenter: Phaser.Scale.CENTER_BOTH
    },
    scene: {
        preload,
        create,
        update
    }
};

let world, player;

function preload() {}

function create() {
    world = new WorldManager(this);
    player = new Player(this, world);
    setupInput(this, player);
}

function update(time, delta) {
    player.moveStep();
}

new Phaser.Game(config);
