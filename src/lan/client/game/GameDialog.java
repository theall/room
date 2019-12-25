package lan.client.game;

import lan.client.game.base.Button;
import lan.client.game.entity.Tank;
import lan.client.game.sprite.Sprite;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class GameDialog extends JDialog implements MouseListener, MouseMotionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyThread myThread;
	private Game game;
	private int fps;
	private Tank tank;
	private Button button;
	private RenderPanel renderPanel;
	private final static int DEF_WIDTH = 800;
	private final static int DEF_HEIGHT = 640;
	public GameDialog(long seed) {
		setDefaultLookAndFeelDecorated(true);
		this.setTitle("Game");// �½��˴���
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);// д��һ�������˳���ʽ
		this.setSize(DEF_WIDTH, DEF_HEIGHT);// ��С
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width-DEF_WIDTH)/2, (screenSize.height-DEF_HEIGHT)/2);
		this.setBackground(Color.BLACK);
		this.setModal(true);
		
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		button = new Button();
		game = new Game(seed);
		game.load();
		tank = game.getGameScene().getObjectLayer().getRandomTank();
		tank.bindButton(button);
		myThread = new MyThread(game, this);

		renderPanel = new RenderPanel(game);
		this.add(renderPanel);

		myThread.start();
	}

	public void setFps(int fps) {
		renderPanel.setFps(fps);
	}

	public void render() {
		renderPanel.updateUI();
	}

	public static void main(String[] args) {
		GameDialog my = new GameDialog(System.currentTimeMillis());
		my.setVisible(true);
		System.exit(0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point dialogPos = getLocationOnScreen();
		Point panelPos = renderPanel.getLocationOnScreen();
		int diffX = panelPos.x-dialogPos.x;
		int diffY = panelPos.y-dialogPos.y;
		int gamePosX = e.getX()-diffX;
		int gamePosY = e.getY()-diffY;
		Sprite sprite = game.getSprite(gamePosX, gamePosY);
		if(sprite != null) {
			tank.bindButton(null);
			tank = (Tank) sprite;
			tank.bindButton(button);
		} else {
			tank.setPos(gamePosX, gamePosY);
		}
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
}
