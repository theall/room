package lan.client.gui;

import lan.client.game.MyThread;
import lan.client.thread.RoomHeadInfo;
import lan.client.thread.WorkThread;
import lan.client.util.ClientInterface;
import lan.client.game.DeomFrame;
import lan.utils.Player;
import lan.utils.Room;
import lan.utils.Team;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

public class RoomDetail extends JFrame implements ClientInterface, MouseListener { // ս������
	private Point mousePos = new Point();// ���������
	private ObjectOutputStream outputStream; // �����
	private RoomDetail.MyPanel myPanel;
	private JButton btnJoin;

	BufferedReader bReader;
	LinkedList<String> msgList = new LinkedList<String>();//

	private class MyPanel extends JPanel {
		public MyPanel() {
			FlowLayout layout = new FlowLayout();
			setLayout(layout);
			JTextField txtName = new JTextField();
			// txtName.setText("client");
			JList listRoom = new JList();

			JPanel leftPanel = new JPanel();
			leftPanel.add(txtName);
			leftPanel.add(listRoom);

			btnJoin = new JButton();
			btnJoin.setText("join");
			listRoom.add(btnJoin);

			btnJoin.addActionListener(new ActionListener() {
				//// ע�����
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

	private static final long serialVersionUID = 1L;
	// ���������õĻ���������Ҫ���õĺ�����
	JPanel panel;
	JScrollPane sPane;
	JTextArea txtContent;
	JLabel lblName, lblSend;
	JTextField txtName, txtSend;
	JButton btnSend;
	JButton button;
	PrintWriter pWriter;

	private JList listBlue; // ���巿������������������
	private JList listRed;
	private WorkThread workThread; // �����߳�

	public RoomDetail(RoomHeadInfo roomHeadInfo, String name) { // �ͻ���ͼ�ν���
		workThread = new WorkThread(roomHeadInfo, name);
		workThread.setClientInterface(this);
		workThread.start();

		myPanel = new MyPanel();
		listBlue = new JList();// new������
		listBlue.addMouseListener(this);
		Object[] BLUE = { "1��", "2��", "3��", "4��", "5��" };
		listBlue.setListData(BLUE);// ������ʵ����
		// listBlue.setBackground( Color.BLUE);//���ö�����ɫ
		listBlue.setSize(100, 200);

		listRed = new JList();
		listRed.addMouseListener(this);
		Object[] Red = { "6��", "7��", "8��", "9��", "10��" };
		listRed.setListData(Red);
		add(listRed);
		listRed.setSize(100, 200);
		// listRed.setBackground( Color.RED);//�����Ǻ���������ɫ
		add(listBlue);// ���ƽ����С

		setSize(480, 360);
		setLocation(400, 240);

		setTitle("ս������"); // ����

		txtContent = new JTextArea();
		// �����ı���ֻ��
		txtContent.setEditable(false);// ���ÿɱ༭Ϊ��
		lblSend = new JLabel("����:"); // �����ı�
		txtSend = new JTextField(20);// �ı���
		btnSend = new JButton("����");// ��ť
		btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
		button = new JButton("��ʼ��Ϸ");

		panel = new JPanel(); // new���ƶ������
		panel.add(lblSend); // �����һ��İ�ť�����ڣ�һ�л��Ƴ���
		panel.add(txtSend);
		panel.add(btnSend);
		panel.add(button);// ����һ����ʼ��ť
		this.add(panel, BorderLayout.SOUTH); // �Ի���������λ�ã����� BorderLayout�߿򲼾�
		this.add(listBlue, BorderLayout.WEST);// ��������ߺ��� ��WEST����
		this.add(listRed, BorderLayout.EAST);// ����
		add(txtContent, BorderLayout.CENTER); // �ϱ�
		// this.setSize(500,300); //��������д�ĳ���
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ��ӹرճ���

		myPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (outputStream == null)
					return;

				try {
					// outputStream.writeObject(e.getPoint());
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ImageIO.write(RoomDetail.getScreen(), "jpg", byteArrayOutputStream);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mousePos = e.getPoint();
				myPanel.updateUI();
			}

			@Override
			public void mouseDragged(MouseEvent e) { // ����϶�
				// TODO Auto-generated method stub

			}
		});

		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String constent = txtSend.getText();

				try {
					workThread.sendMessage(constent); // �����ǽ�����Ϣ
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		button.addActionListener(new ActionListener() {// ��ť���¼�����
			@Override
			public void actionPerformed(ActionEvent e) {
				if (button != null) { // ��Ϊ���ǰ�ť�¼����������������������̶߳�����Frame
					MyThread my = new MyThread(); // ��Ϊ��Frame���޷�ͬ�����������߳�ʵ�ִ���ͬ��
					my.start();// �����߳�
				}
			}
		});
	}

	@Override // ����� ����Ϣ
	public void onMessage(String player, String msg) {// �������Ϣ
		if (msg != null) {
			// SimpleDateFormat ���ڸ�ʽ���࣬�ƶ����ڸ�ʽ
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// ��ȡ��ǰϵͳʱ�䣬��ʹ�����ڸ�ʽ���໪Ϊ�ƶ���ʽ���ַ���
			String strTime = dateFormat.format(new Date());
			// ��ʱ�����Ϣ��ӵ���Ϣ��������
			msgList.addFirst("<==" + strTime + "==>\n" + msg);
			addMsg(strTime);// ���ʱ��
			addMsg(player + "˵��" + msg);// �Ǿ����
		}
		txtSend.setText(""); // ��նԻ�����Ϣ
	}

	@Override
	public void roomRefreshed(Room room) { // ��д�˽ӿڵķ���
		if (room == null) // �������Ϊ�վͷ��ؿ�
			return;

		setTeamToList(room.getBlue(), listBlue); // ���÷���ͬ������
		setTeamToList(room.getRed(), listRed); // ��������ͬ�������б���
	}

	private void setTeamToList(Team team, JList list) { // ��װ����
		Vector<String> vector = new Vector<String>();// �б������ vector
		for (int i = 0; i < team.getCapacity(); i++) { // �����е�Team��ѭ��
			Player player = team.getPlayer(i);// �����Ǹ�playerֵ��������i
			if (player == null) { // ����б����Ϊ��
				vector.add("                ");// ��ӡ���ַ���
			} else {
				vector.add(player.getName());// ������������
			}
		}
		list.setListData(vector); // ʵ���ҵĶ���
	}

	private void addMsg(String msg) {
		txtContent.append(msg); // ���˵�Ļ�
		txtContent.append("\n");// ����
	}

	@Override
	public void mouseClicked(MouseEvent e) { // ���������˫�����뷿��
		JList listTeam = (JList) e.getSource();// �Ŷ��б�
		Team.Type teamType;// �Ŷ�����
		if (listTeam == listRed) {// ����б�����=���
			teamType = Team.Type.RED;// ��ô�͵��ں�
		} else if (listTeam == listBlue) {// ���Ϊ����
			teamType = Team.Type.BLUE;// ��ô������
		} else { // ʲô��û�о�Ϊ��
			return;
		}
		int index = listTeam.getSelectedIndex(); // ������������listѡ��������Ҳ������list���е���
		if (index != -1) { // ���������������1��
			int clickCount = e.getClickCount();// �������
			if (clickCount == 2) { // ���2�ξͽ���
				try {
					workThread.changeTeam(teamType, index);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
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

	public static BufferedImage getScreen() {
		try {
			Robot rb = null; // java.awt.image���е��࣬��������ץȡ��Ļ����������
			rb = new Robot();
			Toolkit tk = Toolkit.getDefaultToolkit(); // ��ȡȱʡ���߰�
			Dimension di = tk.getScreenSize(); // ��Ļ�ߴ���
			Rectangle rec = new Rectangle(0, 0, di.width, di.height);
			BufferedImage bi = rb.createScreenCapture(rec);
			return bi;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
