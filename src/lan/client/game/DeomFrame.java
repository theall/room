package lan.client.game;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;

public class DeomFrame extends JFrame {
	public DeomFrame() {
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
				if (KeyEvent.VK_W == e.getKeyCode()) {// ��W=����
					demo.setY(demo.getY() - 10);// ��Ϊ�ƶ�����������ҲҪ-10
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// ��s����
					demo.setY(demo.getY() + 10);// +10
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// ��A�����ƶ�
					demo.setX(demo.getX() - 10);// ����ƶ�
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// ��D����
					demo.setX(demo.getX() + 10);// +10
				} else if (KeyEvent.VK_J == e.getKeyCode()) {// ͼƬ���
					demo.setWidth(demo.getWidth() + 10);
					demo.setHeight(demo.getHeight() + 10);
				} else if (KeyEvent.VK_K == e.getKeyCode()) {// ͼƬ��С
					demo.setWidth(demo.getWidth() - 10);
					demo.setHeight(demo.getHeight() - 10);

				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	public static void main(String[] args) {
		MyThread my = new MyThread(); // �����̶߳���
		my.start();// �����߳�
	}
}
