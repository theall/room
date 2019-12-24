package lan.client.game;

import lan.client.gui.widget.list.IconText;
import lan.client.gui.widget.list.ImageListModel;

import javax.swing.*;

public class MyThread extends Thread {
	private Game game;
	private GameDialog gameDialog;
	private long lastFrameTime;
	private static float DEFAULT_FRAME_TIME = 1000.0f/60;
	public MyThread(Game game, GameDialog gameDialog) {
		this.game = game;
		this.gameDialog = gameDialog;
	}

	@Override
	public void run() { // 从重写run方法
		lastFrameTime = System.currentTimeMillis();
		int frameCounter = 0;
		int sleptTime = 0;
		while (true) {
			try {
				frameCounter++;
				long frameStartTime = System.currentTimeMillis();//获取系统时间
				game.step();
				gameDialog.render();
				long dt = System.currentTimeMillis() - frameStartTime;
				long sleepTime = (long)(frameCounter*DEFAULT_FRAME_TIME) - sleptTime - dt;
				if(sleepTime > 0) {
					sleptTime += sleepTime;
					Thread.sleep(sleepTime);// 这里利用休眠刷新
				}
				long fakeFrameTime = System.currentTimeMillis() - frameStartTime;
				int fps = (int)(1000.0/fakeFrameTime);
				gameDialog.setFps(fps);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
