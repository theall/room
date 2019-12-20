package lan.client.game;

import lan.client.game.sprite.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DemoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Tank tank;
	private int fps;
	public DemoPanel() {
	}

	public void paint(Graphics g) { // paint绘制函数
		super.paint(g); // 调用父类函数

		tank.render(g);
		g.drawString("FPS:"+fps, 0, 20);//调用坦克的对象在面板绘制FPS
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public void setGameObject(Tank tank) {
		this.tank = tank;
	}
}
