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

public class RoomDetail extends JFrame implements ClientInterface, MouseListener { // 战斗界面
	private Point mousePos = new Point();// 定义鼠标点击
	private ObjectOutputStream outputStream; // 输出流
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
				//// 注册监听
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
	// 这里我设置的绘制区域需要调用的函数名
	JPanel panel;
	JScrollPane sPane;
	JTextArea txtContent;
	JLabel lblName, lblSend;
	JTextField txtName, txtSend;
	JButton btnSend;
	JButton button;
	PrintWriter pWriter;

	private JList listBlue; // 定义房间界面变量方便后面调用
	private JList listRed;
	private WorkThread workThread; // 工作线程

	public RoomDetail(RoomHeadInfo roomHeadInfo, String name) { // 客户端图形界面
		workThread = new WorkThread(roomHeadInfo, name);
		workThread.setClientInterface(this);
		workThread.start();

		myPanel = new MyPanel();
		listBlue = new JList();// new出对象
		listBlue.addMouseListener(this);
		Object[] BLUE = { "1号", "2号", "3号", "4号", "5号" };
		listBlue.setListData(BLUE);// 这里是实例化
		// listBlue.setBackground( Color.BLUE);//设置队伍颜色
		listBlue.setSize(100, 200);

		listRed = new JList();
		listRed.addMouseListener(this);
		Object[] Red = { "6号", "7号", "8号", "9号", "10号" };
		listRed.setListData(Red);
		add(listRed);
		listRed.setSize(100, 200);
		// listRed.setBackground( Color.RED);//这里是红蓝队伍颜色
		add(listBlue);// 绘制界面大小

		setSize(480, 360);
		setLocation(400, 240);

		setTitle("战斗房间"); // 标题

		txtContent = new JTextArea();
		// 设置文本域只读
		txtContent.setEditable(false);// 设置可编辑为假
		lblSend = new JLabel("发言:"); // 发言文本
		txtSend = new JTextField(20);// 文本框
		btnSend = new JButton("发送");// 按钮
		btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
		button = new JButton("开始游戏");

		panel = new JPanel(); // new绘制对象出来
		panel.add(lblSend); // 进行我画的按钮，窗口，一切绘制出来
		panel.add(txtSend);
		panel.add(btnSend);
		panel.add(button);// 绘制一个开始按钮
		this.add(panel, BorderLayout.SOUTH); // 对话框跟队伍的位置，中心 BorderLayout边框布局
		this.add(listBlue, BorderLayout.WEST);// 队伍在左边函数 ，WEST西边
		this.add(listRed, BorderLayout.EAST);// 北边
		add(txtContent, BorderLayout.CENTER); // 南边
		// this.setSize(500,300); //这里是我写的长宽
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 添加关闭程序

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
			public void mouseDragged(MouseEvent e) { // 鼠标拖动
				// TODO Auto-generated method stub

			}
		});

		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String constent = txtSend.getText();

				try {
					workThread.sendMessage(constent); // 这里是接受消息
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		button.addActionListener(new ActionListener() {// 按钮的事件监听
			@Override
			public void actionPerformed(ActionEvent e) {
				if (button != null) { // 因为我是按钮事件监听所以是在这里启动线程而不是Frame
					MyThread my = new MyThread(); // 因为在Frame中无法同步所以利用线程实现窗口同步
					my.start();// 启动线程
				}
			}
		});
	}

	@Override // ，玩家 和消息
	public void onMessage(String player, String msg) {// 上面的消息
		if (msg != null) {
			// SimpleDateFormat 日期格式化类，制定日期格式
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 获取当前系统时间，并使用日期格式化类华为制定格式读字符串
			String strTime = dateFormat.format(new Date());
			// 将时间和信息添加到信息链表集合中
			msgList.addFirst("<==" + strTime + "==>\n" + msg);
			addMsg(strTime);// 输出时间
			addMsg(player + "说：" + msg);// 那就输出
		}
		txtSend.setText(""); // 清空对话框消息
	}

	@Override
	public void roomRefreshed(Room room) { // 重写了接口的方法
		if (room == null) // 如果房间为空就返回空
			return;

		setTeamToList(room.getBlue(), listBlue); // 调用方法同步名字
		setTeamToList(room.getRed(), listRed); // 房间名字同步到了列表中
	}

	private void setTeamToList(Team team, JList list) { // 封装函数
		Vector<String> vector = new Vector<String>();// 列表定义变量 vector
		for (int i = 0; i < team.getCapacity(); i++) { // 在所有的Team中循环
			Player player = team.getPlayer(i);// 这里是给player值等于索引i
			if (player == null) { // 如果列表玩家为空
				vector.add("                ");// 打印空字符串
			} else {
				vector.add(player.getName());// 否则就输出名字
			}
		}
		list.setListData(vector); // 实列我的队伍
	}

	private void addMsg(String msg) {
		txtContent.append(msg); // 输出说的话
		txtContent.append("\n");// 换行
	}

	@Override
	public void mouseClicked(MouseEvent e) { // 这里是鼠标双击进入房间
		JList listTeam = (JList) e.getSource();// 团队列表
		Team.Type teamType;// 团队类型
		if (listTeam == listRed) {// 如果列表的玩家=红队
			teamType = Team.Type.RED;// 那么就等于红
		} else if (listTeam == listBlue) {// 如果为蓝队
			teamType = Team.Type.BLUE;// 那么就是蓝
		} else { // 什么都没有就为空
			return;
		}
		int index = listTeam.getSelectedIndex(); // 定义了索引，list选择索引，也就是在list当中点点点
		if (index != -1) { // 如果索引到不少于1次
			int clickCount = e.getClickCount();// 点击计数
			if (clickCount == 2) { // 如果2次就进入
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
}
