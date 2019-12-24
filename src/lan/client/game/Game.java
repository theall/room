package lan.client.game;

import lan.client.game.base.GameObject;
import lan.client.game.base.RandomWrapper;
import lan.client.game.scene.GameScene;

import java.awt.Graphics;

public class Game extends GameObject {
	private GameScene gameScene;

	public Game(long seed) {
        RandomWrapper.initialize(seed);
		gameScene = new GameScene();
	}

	public void load() {
		gameScene.load();
	}

	public GameScene getGameScene() {
		return gameScene;
	}

	@Override
	public void step() {//把数据写这里
		gameScene.step();
	}

	@Override
	public void render(Graphics g) {
		gameScene.render(g);
	}
}
