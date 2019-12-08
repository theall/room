package lan.client.game;

public class MyThread extends Thread {
	private DemoFrame demo;

	public MyThread(DemoFrame frame) {
		demo = frame;
	}
	@Override
	public void run() { // ����дrun����
		while (true) {
			try {
				Thread.sleep(5);// ������������ˢ��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			demo.repaint();// ˢ��
		}
	}

}
