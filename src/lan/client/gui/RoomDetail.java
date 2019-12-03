package lan.client.gui;

import lan.client.game.MyThread;
import lan.client.gui.widget.list.ImageCellRender;
import lan.client.gui.widget.list.MyJList;
import lan.client.thread.RoomHeadInfo;
import lan.client.thread.WorkThread;
import lan.client.util.ClientInterface;
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
import java.util.ArrayList;
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
			MyJList listRoom = new MyJList();

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
	JLabel lblName, lblSend;//�ı���ǩ
	JTextField txtName, txtSend;
	JButton btnSend;
	JButton btnStart;
	private JButton btnChoosePlayer;//����ѡ�˰�ť
	PrintWriter pWriter;
	private MyJList listBlue; // ���巿������������������
	private MyJList listRed;
	private WorkThread workThread; // �����߳�

	public RoomDetail(RoomHeadInfo roomHeadInfo, String name) { // �ͻ���ͼ�ν���
		workThread = new WorkThread(roomHeadInfo, name);
		workThread.setClientInterface(this);
		workThread.start();

		myPanel = new MyPanel();
		listBlue = new MyJList();// new������
		listBlue.addMouseListener(this);
		Object[] BLUE = { "1��", "2��", "3��", "4��", "5��" };
		listBlue.setListData(BLUE);// ������ʵ����
		// listBlue.setBackground( Color.BLUE);//���ö�����ɫ
		listBlue.setSize(100, 200);

		listRed = new MyJList();
		listRed.addMouseListener(this);
		Object[] Red = { "6��", "7��", "8��", "9��", "10��" };
		listRed.setListData(Red);
		add(listRed);
		listRed.setSize(100, 200);
		// listRed.setBackground( Color.RED);//�����Ǻ���������ɫ
		add(listBlue);// ���ƽ����С

		setSize(680, 460);
		setLocation(400, 240);
		setTitle("ս������"); // ����

		txtContent = new JTextArea();
		// �����ı���ֻ��
		txtContent.setEditable(false);// ���ÿɱ༭Ϊ��
		lblSend = new JLabel("����:"); // �����ı�
		txtSend = new JTextField(20);// �ı���
		btnSend = new JButton("����");// ��ť
		btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
		btnStart = new JButton("��ʼ��Ϸ");
		lblName = new JLabel("�����б�");//������һ���ı���ǩ
		btnChoosePlayer = new JButton("ѡ���ɫ");

		panel = new JPanel(); // new���ƶ������
		panel.add(lblSend); // �����һ��İ�ť�����ڣ�һ�л��Ƴ���
		panel.add(txtSend);
		panel.add(btnSend);
		panel.add(btnStart);// ����һ����ʼ��ť
		panel.add(lblName);//����ͷ���б���Ϣ
		panel.add(btnChoosePlayer);

		this.add(panel, BorderLayout.SOUTH); // �Ի���������λ�ã��ϱ� BorderLayout�߿򲼾�
		this.add(listBlue, BorderLayout.WEST);// ��������ߺ��� ��WEST����
		this.add(listRed, BorderLayout.EAST);// ����
		this.add(lblName,BorderLayout. NORTH);//���ߣ�Ҳ�����ö�
		add(txtContent, BorderLayout.CENTER); // ����
		// this.setSize(500,300); //��������д�ĳ���
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ��ӹرճ���

		File[] files = new File("src/resources").listFiles(new FileFilter() {
			public boolean accept(File file) {      //����������쳣�������ļ�·�����ˣ������ļ���ʧ
				return file.getName().endsWith("jpg");
			}
		});
		ArrayList<ImageIcon> iconArrayList = new ArrayList<>();
		for (File file : files) {
			ImageIcon icon = new ImageIcon(file.getPath());
			iconArrayList.add(icon);
		}
		ImageCellRender.setCharacters(iconArrayList);

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

		btnStart.addActionListener(new ActionListener() {// ��ť���¼�����
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean seedSend = workThread.sendSeed();
				if(seedSend)
					btnStart.setEnabled(false);
			}
		});

		btnChoosePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnChoosePlayer!=null){
					new ChooseCharacterDialog().setVisible(true);
				}
			}
		});
		btnChoosePlayer.addMouseListener(new MouseListener() { //��������������Ҫ�ҵ��ͼƬ�ļ����¼���ʵ�ֺܶ෽��
			@Override
			public void mouseClicked(MouseEvent e) {
				//int index = btnChoosePlayer.getSelectedIndex();

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

	private void setTeamToList(Team team, MyJList list) { // ��װ����
		ArrayList<String> nameList = new ArrayList<String>();// �б������ vector
		ArrayList<Integer> idList = new ArrayList<>();
		for (int i = 0; i < team.getCapacity(); i++) { // �����е�Team��ѭ��
			Player player = team.getPlayer(i);// �����Ǹ�playerֵ��������i

			if (player == null) { // ����б����Ϊ��
				nameList.add("------------------");// ��ӡ���ַ���
			} else {
				nameList.add(player.getName());// ������������
			}
			idList.add(1);
		}
		list.setListData(idList, nameList);
	}

	private void addMsg(String msg) {
		txtContent.append(msg); // ���˵�Ļ�
		txtContent.append("\n");// ����
	}

	@Override
	public void mouseClicked(MouseEvent e) { // ���������˫�����뷿��
		MyJList listTeam = (MyJList) e.getSource();// �Ŷ��б�
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
	@Override
	public void onSeed(long seed) {
		System.out.println("Seed received, start game: " + seed);
		// TODO �Զ����ɵķ������
		MyThread thread = new MyThread();
		thread.start();
	}
}
