package lan.client.game;

import lan.client.game.base.Button;
import lan.client.game.base.Dir;
import lan.client.game.entity.Tank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class DemoFrame extends JFrame implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyThread myThread;
	private Random random;
	private Game game;
	private Tank tank;
	private Button button;
	public DemoFrame() {
		button = new Button();
		game = new Game();
		game.load();
		tank = game.getGameScene().getObjectLayer().getRandomTank();
		tank.bindButton(button);

		DemoPanel panel = new DemoPanel();// ʵ����DemoPanel
		myThread = new MyThread(game, panel);

		this.setTitle("Game");// �½��˴���
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// д��һ�������˳���ʽ
		this.setSize(1000, 500);// ��С
		this.setLocation(400, 300);
		Game demo = new Game();// ����demo���еĶ���

		this.add(panel); // ���ͼƬ������
		this.setVisible(true);// �����ǰɴ�����ʾ�ƶ���Demo��ʵ�����Ժ󣬷�ֹ������ʾǰDemoû�б�����
		// ������д��һ�����̼���
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {// ���̼���
				Button.Type type = null;
				if (KeyEvent.VK_W == e.getKeyCode()) {// ��W=����
					type = Button.Type.UP;
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// ��s����
					type = Button.Type.DOWN;
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// ��A�����ƶ�
					type = Button.Type.LEFT;
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// ��D����
					type = Button.Type.RIGHT;
				}
				if(type != null)
					button.set(type, true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Button.Type type = null;
				if (KeyEvent.VK_W == e.getKeyCode()) {// ��W=����
					type = Button.Type.UP;
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// ��s����
					type = Button.Type.DOWN;
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// ��A�����ƶ�
					type = Button.Type.LEFT;
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// ��D����
					type = Button.Type.RIGHT;
				}
				if(type != null)
					button.set(type, false);
			}
		});
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		tank.setPos(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setTitle("" + e.getX() + "." + e.getY());
	}
}
