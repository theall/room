package lan.server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ServerGui extends JFrame {
	private Point mousePos = new Point();
	private MyPanel canvas;
	private Dimension screenSize;
	private Image image;
	
	private class MyPanel extends JPanel {
		public MyPanel() {
			FlowLayout layout = new FlowLayout();
			setLayout(layout);
			JButton button = new JButton();
			button.setText("create");
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			add(button);
		}
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if(mousePos != null)
				g.fillRect(mousePos.x - 2, mousePos.y - 2, 5, 5);
			
			if(image != null) {
				Dimension windowSizeDimension = getSize();
				g.drawImage(image, 0, 0, windowSizeDimension.width, windowSizeDimension.height, null);
			}
		}
	}

	public ServerGui() {
		setTitle("canvas");
		setSize(480, 320);
		setLocation(100, 200);
		canvas = new MyPanel();
		add(canvas);
		//setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SocketThread thread = new SocketThread(this);
		thread.start();
	}

	class SocketThread extends Thread {
		private OutputStream outputStream;
		private ServerGui gui;

		public SocketThread(ServerGui gui) {
			this.gui = gui;
		}

		@Override
		public void run() {
			try {
				gui.setTitle("Listening");
				ServerSocket socket = new ServerSocket(44445);
				Socket client = socket.accept();
				gui.setTitle("Connected");
				ObjectInputStream in = new ObjectInputStream(client.getInputStream());
				while (true) {
//					Point pos = (Point)in.readObject();
//					gui.setCursorPos(pos);
//					ImagePacket packet = (ImagePacket)in.readObject();
//
//					ByteArrayInputStream bin = new ByteArrayInputStream((byte[])packet.getData());  
//			        BufferedImage image = ImageIO.read(bin);  
//					gui.setImage(image);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setCursorPos(Point pos) {
		mousePos.x = pos.x;
		mousePos.y = pos.y;
		canvas.updateUI();
		
		Point globalPos = new Point();
		globalPos.x = (int)((float)pos.x/getSize().width * screenSize.width);
		globalPos.y = (int)((float)pos.y/getSize().height * screenSize.height);
		setLocation(globalPos);
	}

	public void setImage(Image image) {
		this.image = image;
		canvas.updateUI();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerGui gui = new ServerGui();
		gui.setVisible(true);
	}

}
