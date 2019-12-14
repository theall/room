package lan.client.game;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class DemoFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyThread myThread;
	private Random random;

	public DemoFrame() {
		myThread = new MyThread(this);
		myThread.start();

		this.setTitle("Game");// �½��˴���
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// д��һ�������˳���ʽ
		this.setSize(500, 300);// ��С
		Demo demo = new Demo();// ����demo���еĶ���
		DemoPanel panel = new DemoPanel(demo);// ʵ����DemoPanel
		this.add(panel); // ���ͼƬ������
		this.setVisible(true);// �����ǰɴ�����ʾ�ƶ���Demo��ʵ�����Ժ󣬷�ֹ������ʾǰDemoû�б�����
		// ������д��һ�����̼���
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {// ���̼���
				int step = getRandStep();
				if (KeyEvent.VK_W == e.getKeyCode()) {// ��W=����
					demo.setY(demo.getY() - step);// ��Ϊ�ƶ�����������ҲҪ-step
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// ��s����
					demo.setY(demo.getY() + step);// +step
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// ��A�����ƶ�
					demo.setX(demo.getX() - step);// ����ƶ�
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// ��D����
					demo.setX(demo.getX() + step);// +step
				} else if (KeyEvent.VK_J == e.getKeyCode()) {// ͼƬ���
					demo.setWidth(demo.getWidth() + step);
					demo.setHeight(demo.getHeight() + step);
				} else if (KeyEvent.VK_K == e.getKeyCode()) {// ͼƬ��С
					demo.setWidth(demo.getWidth() - step);
					demo.setHeight(demo.getHeight() - step);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	private int getRandStep() {//�Ѷ���д��Step��Ҳ������֡д��Ϸ
		return random.nextInt(9) + 1;
	}

	public void setSeed(long seed) { //������Ϸʱ֪ͨ
		random = new Random(seed);
	}

	public static void main(String[] args) {
		DemoFrame my = new DemoFrame();
		my.setVisible(true);
	}
}
