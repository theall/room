package lan.client.game;

import lan.client.game.sprite.Dir;
import lan.client.game.sprite.Tank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class DemoFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyThread myThread;
	private Random random;
	private Tank tank;
	public DemoFrame() {
		tank = new Tank();
		BufferedImage tankImage = null;
		try {
			tankImage = ImageIO.read(new FileInputStream("F:\\Warchariot\\7.png"));//��ͼƬ
		} catch (IOException e) {
			e.printStackTrace();
		}
		tank.load(tankImage);
		tank.setPos(100, 100);//̹��λ��
		DemoPanel panel = new DemoPanel();// ʵ����DemoPanel
		myThread = new MyThread(tank, panel);

		this.setTitle("Game");// �½��˴���
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// д��һ�������˳���ʽ
		this.setSize(1000, 500);// ��С
		Demo demo = new Demo();// ����demo���еĶ���

		this.add(panel); // ���ͼƬ������
		this.setVisible(true);// �����ǰɴ�����ʾ�ƶ���Demo��ʵ�����Ժ󣬷�ֹ������ʾǰDemoû�б�����
		// ������д��һ�����̼���
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {// ���̼���
				int step = 10;//getRandStep();
				Dir dir = null;
				if (KeyEvent.VK_W == e.getKeyCode()) {// ��W=����
					dir = Dir.UP;
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// ��s����
					dir = Dir.DOWN;
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// ��A�����ƶ�
					dir = Dir.LEFT;
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// ��D����
					dir = Dir.RIGHT;
				} else if (KeyEvent.VK_J == e.getKeyCode()) {// ͼƬ���
					demo.setWidth(demo.getWidth() + step);
					demo.setHeight(demo.getHeight() + step);
				} else if (KeyEvent.VK_K == e.getKeyCode()) {// ͼƬ��С
					demo.setWidth(demo.getWidth() - step);
					demo.setHeight(demo.getHeight() - step);
				}
				if(dir != null)
					tank.setDir(dir);
			}
			public void moveDir(Dir dir){
				int step = 10;
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	public void start() {
		myThread.start();
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

		my.start();
	}
}
