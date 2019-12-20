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
			tankImage = ImageIO.read(new FileInputStream("F:\\Warchariot\\7.png"));//传图片
		} catch (IOException e) {
			e.printStackTrace();
		}
		tank.load(tankImage);
		tank.setPos(100, 100);//坦克位置
		DemoPanel panel = new DemoPanel();// 实例化DemoPanel
		myThread = new MyThread(tank, panel);

		this.setTitle("Game");// 新建了窗体
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 写了一个窗体退出方式
		this.setSize(1000, 500);// 大小
		Demo demo = new Demo();// 创建demo类中的对象

		this.add(panel); // 添加图片到窗口
		this.setVisible(true);// 这里是吧窗口显示移动到Demo类实例化以后，防止窗口显示前Demo没有被加载
		// 这里我写了一个键盘监听
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {// 键盘监听
				int step = 10;//getRandStep();
				Dir dir = null;
				if (KeyEvent.VK_W == e.getKeyCode()) {// 按W=上移
					dir = Dir.UP;
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// 按s向下
					dir = Dir.DOWN;
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// 按A向左移动
					dir = Dir.LEFT;
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// 按D向右
					dir = Dir.RIGHT;
				} else if (KeyEvent.VK_J == e.getKeyCode()) {// 图片变大
					demo.setWidth(demo.getWidth() + step);
					demo.setHeight(demo.getHeight() + step);
				} else if (KeyEvent.VK_K == e.getKeyCode()) {// 图片变小
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
}
