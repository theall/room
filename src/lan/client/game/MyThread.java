package lan.client.game;

import lan.client.game.sprite.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MyThread extends Thread {
	private Tank tank;
	private DemoPanel panel;
	private long lastFrameTime;

	public MyThread(Tank tank, DemoPanel panel) {
		this.tank = tank;
		this.panel = panel;
		panel.setGameObject(tank);
	}

	@Override
	public void run() { // 从重写run方法
		lastFrameTime = System.currentTimeMillis();
		int franeCounter = 0;
		while (true) {
			try {
				franeCounter++;
				long currentTime = System.currentTimeMillis();//获取系统时间

				tank.step();
				long frameTime = currentTime - lastFrameTime;
				lastFrameTime = currentTime;
				int fps = (int)(1000.0 / frameTime);
				panel.setFps(fps);
				panel.updateUI();
				Thread.sleep(33);// 这里利用休眠刷新
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
