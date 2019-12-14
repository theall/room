package lan.client.game;

import javax.swing.*;
import java.awt.*;

public class DemoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Demo demo;
	
	public DemoPanel(Demo demo) {
		this.demo = demo;
	}

	public void paint(Graphics g) { // paint���ƺ���
		super.paint(g); // ���ø��ຯ��
		g.drawImage(demo.getImage(), demo.getX(), demo.getY(), demo.getWidth(), demo.getHeight(), null);

	}

}
