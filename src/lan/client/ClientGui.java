package lan.client;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.javafx.geom.AreaOp.AddOp;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import javafx.scene.shape.Box;

public class ClientGui extends JFrame {
	private Point mousePos = new Point();
	private ObjectOutputStream outputStream;
	private MyPanel myPanel;
	private JButton btnJoin;
	
	private class MyPanel extends JPanel {
		public MyPanel() {
			FlowLayout layout = new FlowLayout();
			setLayout(layout);
			JTextField txtName = new JTextField();
			txtName.setText("client");
			JList listRoom = new JList();
			
			JPanel leftPanel = new JPanel();
			leftPanel.add(txtName);
			leftPanel.add(listRoom);
			
		    btnJoin = new JButton();
			btnJoin.setText("join");
			listRoom.add(btnJoin);
			
			btnJoin.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			add(leftPanel);
			pack();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (mousePos != null)
				g.fillRect(mousePos.x - 2, mousePos.y - 2, 5, 5);

		}
	}

	private void startSocket() {
		Socket socket;
		try {
			socket = new Socket("localhost", 44445);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ClientGui() {
		myPanel = new MyPanel();
		add(myPanel);
		setTitle("Client");
		setSize(480, 320);
		setLocation(650, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		myPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (outputStream == null)
					return;

				try {
					// outputStream.writeObject(e.getPoint());
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ImageIO.write(ClientGui.getScreen(), "jpg", byteArrayOutputStream);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mousePos = e.getPoint();
				myPanel.updateUI();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static BufferedImage getScreen() {
		try {
			Robot rb = null; // java.awt.image包中的类，可以用来抓取屏幕，即截屏。
			rb = new Robot();
			Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
			Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
			Rectangle rec = new Rectangle(0, 0, di.width, di.height);
			BufferedImage bi = rb.createScreenCapture(rec);
			return bi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		ClientGui clientGui = new ClientGui();
		clientGui.setVisible(true);
	}

}
