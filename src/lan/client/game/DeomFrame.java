package lan.client.game;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;

public class DeomFrame extends JFrame {
	public DeomFrame() {
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
				if (KeyEvent.VK_W == e.getKeyCode()) {// 按W=上移
					demo.setY(demo.getY() - 10);// 因为移动所以纵坐标也要-10
				} else if (KeyEvent.VK_S == e.getKeyCode()) {// 按s向下
					demo.setY(demo.getY() + 10);// +10
				} else if (KeyEvent.VK_A == e.getKeyCode()) {// 按A向左移动
					demo.setX(demo.getX() - 10);// 左边移动
				} else if (KeyEvent.VK_D == e.getKeyCode()) {// 按D向右
					demo.setX(demo.getX() + 10);// +10
				} else if (KeyEvent.VK_J == e.getKeyCode()) {// 图片变大
					demo.setWidth(demo.getWidth() + 10);
					demo.setHeight(demo.getHeight() + 10);
				} else if (KeyEvent.VK_K == e.getKeyCode()) {// 图片变小
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
		MyThread my = new MyThread(); // 创建线程对象
		my.start();// 启动线程
	}
}
