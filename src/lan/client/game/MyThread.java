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
	public void run() { // ����дrun����
		lastFrameTime = System.currentTimeMillis();
		int franeCounter = 0;
		while (true) {
			try {
				franeCounter++;
				long currentTime = System.currentTimeMillis();//��ȡϵͳʱ��

				game.step();
				long frameTime = currentTime - lastFrameTime;
				lastFrameTime = currentTime;
				int fps = (int)(1000.0 / frameTime);
				panel.setFps(fps);
				panel.updateUI();
				Thread.sleep(17);// ������������ˢ��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
