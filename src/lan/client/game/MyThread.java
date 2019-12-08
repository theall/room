package lan.client.game;

public class MyThread extends Thread {
	private DemoFrame demo;

	public MyThread(DemoFrame frame) {
		demo = frame;
	}
	@Override
	public void run() { // 从重写run方法
		while (true) {
			try {
				Thread.sleep(5);// 这里利用休眠刷新
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			demo.repaint();// 刷新
		}
	}

}
