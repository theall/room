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

		DemoPanel panel = new DemoPanel();// 实例化DemoPanel
		myThread = new MyThread(game, panel);

		this.setTitle("Game");// 新建了窗体
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 写了一个窗体退出方式
		this.setSize(1000, 500);// 大小
		this.setLocation(400, 300);
		Game demo = new Game();// 创建demo类中的对象

		this.add(panel); // 添加图片到窗口
		this.setVisible(true);// 这里是吧窗口显示移动到Demo类实例化以后，防止窗口显示前Demo没有被加载
		// 这里我写了一个键盘监听
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {// 键盘监听
				Button.Type type = null;
				if (KeyEvent.VK_W == e.getKeyCode()) {// 按W=上移
					type = Button.Type.UP;
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// 按s向下
					type = Button.Type.DOWN;
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// 按A向左移动
					type = Button.Type.LEFT;
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// 按D向右
					type = Button.Type.RIGHT;
				}
				if(type != null)
					button.set(type, true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				Button.Type type = null;
				if (KeyEvent.VK_W == e.getKeyCode()) {// 按W=上移
					type = Button.Type.UP;
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// 按s向下
					type = Button.Type.DOWN;
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// 按A向左移动
					type = Button.Type.LEFT;
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// 按D向右
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

	private int getRandStep() {//把动作写入Step中也就是用帧写游戏
		return random.nextInt(9) + 1;
	}

	public void setSeed(long seed) { //启动游戏时通知
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
