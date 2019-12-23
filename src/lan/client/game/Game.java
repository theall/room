package lan.client.game;

import lan.client.game.base.GameObject;
import lan.client.game.scene.GameScene;

import java.awt.Graphics;
import java.awt.Image;

public class Game extends GameObject {
	private GameScene gameScene;

	public Game() {
		gameScene = new GameScene();
	}

	public void load() {
		gameScene.load();
	}

	public GameScene getGameScene() {
		return gameScene;
	}

	@Override
	public void step() {//������д����
		gameScene.step();
	}

	@Override
	public void render(Graphics g) {
		gameScene.render(g);
	}
}
