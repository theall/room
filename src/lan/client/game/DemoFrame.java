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

		this.setTitle("Game");// 新建了窗体
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 写了一个窗体退出方式
		this.setSize(500, 300);// 大小
		Demo demo = new Demo();// 创建demo类中的对象
		DemoPanel panel = new DemoPanel(demo);// 实例化DemoPanel
		this.add(panel); // 添加图片到窗口
		this.setVisible(true);// 这里是吧窗口显示移动到Demo类实例化以后，防止窗口显示前Demo没有被加载
		// 这里我写了一个键盘监听
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {// 键盘监听
				int step = getRandStep();
				if (KeyEvent.VK_W == e.getKeyCode()) {// 按W=上移
					demo.setY(demo.getY() - step);// 因为移动所以纵坐标也要-step
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// 按s向下
					demo.setY(demo.getY() + step);// +step
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// 按A向左移动
					demo.setX(demo.getX() - step);// 左边移动
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// 按D向右
					demo.setX(demo.getX() + step);// +step
				} else if (KeyEvent.VK_J == e.getKeyCode()) {// 图片变大
					demo.setWidth(demo.getWidth() + step);
					demo.setHeight(demo.getHeight() + step);
				} else if (KeyEvent.VK_K == e.getKeyCode()) {// 图片变小
					demo.setWidth(demo.getWidth() - step);
					demo.setHeight(demo.getHeight() - step);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
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
	}
}
