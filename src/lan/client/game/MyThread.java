package lan.client.game;

public class MyThread extends Thread {
	private Game game;
	private DemoPanel panel;
	private long lastFrameTime;

	public MyThread(Game game, DemoPanel panel) {
		this.game = game;
		this.panel = panel;
		panel.setGameObject(game);
	}

	@Override
	public void run() { // 从重写run方法
		lastFrameTime = System.currentTimeMillis();
		int franeCounter = 0;
		while (true) {
			try {
				franeCounter++;
				long currentTime = System.currentTimeMillis();//获取系统时间

				game.step();
				long frameTime = currentTime - lastFrameTime;
				lastFrameTime = currentTime;
				int fps = (int)(1000.0 / frameTime);
				panel.setFps(fps);
				panel.updateUI();
				Thread.sleep(17);// 这里利用休眠刷新
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
