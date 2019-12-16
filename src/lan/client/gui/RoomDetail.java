package lan.client.gui;

import lan.client.game.DemoFrame;
import lan.client.gui.widget.list.IconText;
import lan.client.gui.widget.list.ImageCellRender;
import lan.client.gui.widget.list.MyJList;
import lan.client.thread.WorkThread;
import lan.client.util.ClientInterface;
import lan.utils.Player;
import lan.utils.Room;
import lan.utils.Team;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.*;

public class RoomDetail extends JDialog
		implements ClientInterface, MouseListener, ActionListener, KeyListener, WindowListener { // 战斗界面
	BufferedReader bReader;
	LinkedList<String> msgList = new LinkedList<String>();

	private static final long serialVersionUID = 1L;
	// 这里我设置的绘制区域需要调用的函数名
	private JPanel panel;
	private JTextArea txtContent;// 打字框
	private JLabel lblName, lblSend;// 文本标签
	private JTextField txtSend;
	private JButton btnSend;
	private JButton btnStart;
	private JPopupMenu popupMenu;
	private JMenuItem menuKick;
	private JMenuItem menuSetOwner;
	private JButton btnChoosePlayer;// 定义选人按钮
	private MyJList listBlue; // 定义房间界面变量方便后面调用
	private MyJList listRed;
	private JScrollPane scrollPanel;

	private int roomOwnerId = -1;
	private WorkThread workThread; // 工作线程

	public RoomDetail(String host, int port, String name) { // 客户端图形界面
		initialize();

		workThread = new WorkThread(host, port, name);
		workThread.setClientInterface(this);
		workThread.start();
	}

	public void initialize() {
		setTitle("战斗房间"); // 标题
		setModal(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(680, 460);
		setLocation(400, 240);
		listBlue = createList();
		listRed = createList();

		scrollPanel = new JScrollPane(txtContent);
		txtContent = new JTextArea(20, 50);// 组件大小
		// 设置文本域只读
		txtContent.setEditable(false);// 设置可编辑为假
		lblSend = new JLabel("发言:"); // 发言文本
		txtSend = new JTextField(20);// 文本框
		btnSend = new JButton("发送");// 按钮
		btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
		btnStart = new JButton("开始游戏");
		lblName = new JLabel("队伍列表");// 创建了一个文本标签
		btnChoosePlayer = new JButton("选择角色");

		panel = new JPanel(); // new绘制对象出来
		panel.add(lblSend); // 进行我画的按钮，窗口，一切绘制出来
		panel.add(txtSend);
		panel.add(btnSend);
		panel.add(btnStart);// 绘制一个开始按钮
		panel.add(lblName);// 绘制头顶列表信息
		panel.add(btnChoosePlayer);
		panel.add(scrollPanel);

		this.add(panel, BorderLayout.SOUTH); // 对话框跟队伍的位置，南边 BorderLayout边框布局
		this.add(listBlue, BorderLayout.WEST);// 队伍在左边函数 ，WEST西边
		this.add(listRed, BorderLayout.EAST);// 东边
		this.add(lblName, BorderLayout.NORTH);// 北边，也就是置顶
		this.add(scrollPanel, BorderLayout.CENTER);// scrollPane就是一个容器
		this.scrollPanel.setViewportView(txtContent);// 显示容器中的文本框
		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		loadIcons();
		initPopMenu();
		
		btnSend.addActionListener(this);
		btnStart.addActionListener(this);
		btnChoosePlayer.addActionListener(this);
		txtSend.addKeyListener(this);
		addWindowListener((WindowListener) this);
	}

	private MyJList createList() {// 把列表封装成函数然后调用
		MyJList list = new MyJList();
		list.addMouseListener(this);
		Object[] Red = { "1号", "2号", "3号", "4号", "5号" };
		list.setListData(Red);
		add(list);
		list.setSize(100, 200);
		return list;
	}

	private void initPopMenu() {
		popupMenu = new JPopupMenu();// 在方法中拿到JPopupMenu类的对象
		menuKick = new JMenuItem("踢出");// 菜单列表
		menuSetOwner = new JMenuItem("转移房主");// 菜单列表
		popupMenu.add(menuKick);// 跟jPanel一样先注册在绘制
		popupMenu.addSeparator();// 分割线
		popupMenu.add(menuSetOwner);
		menuKick.addActionListener(this);
		menuSetOwner.addActionListener(this);
	}

	private void loadIcons() {
		ArrayList<ImageIcon> iconArrayList = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			String imageName = String.format("/resources/%d.jpg", i);
			URL imageURL = this.getClass().getResource(imageName);
			ImageIcon imageIcon = new ImageIcon(imageURL);
			iconArrayList.add(imageIcon);
		}

		ImageCellRender.setCharacters(iconArrayList);
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

		roomOwnerId = room.getOwner();// 这里是在房间里拿到了房主的ID
		setTeamToList(room.getBlue(), listBlue); // 调用方法同步名字
		setTeamToList(room.getRed(), listRed); // 房间名字同步到了列表中
	}

	private void setTeamToList(Team team, MyJList list) { // 封装函数
		int myId = workThread.getMyId();
		ArrayList<IconText> iconTexts = new ArrayList<IconText>();
		for (int i = 0; i < team.getCapacity(); i++) { // 在所有的Team中循环
			Player player = team.getPlayer(i);// 这里是给player值等于索引i
			IconText iconText = new IconText();

			if (player != null) { // 如果列表玩家为空
				iconText.setText(player.getName());
				iconText.setIcon(player.getRoleId());

				int playerId = player.getId();
				// Check if it's me
				if (playerId == roomOwnerId) {
					iconText.setColor(Color.red);
				} else if (playerId == myId) {
					iconText.setColor(Color.green);
				}
			}
			iconTexts.add(iconText);
		}
		list.setListData(iconTexts);
	}

	private void addMsg(String msg) {
		txtContent.append(msg); // 输出说的话
		txtContent.append("\n");// 换行
		txtContent.setCaretPosition(txtContent.getText().length());
	}

	private void sendContentToServer() {
		String constent = txtSend.getText().trim();
		txtSend.setText("");
		if(constent.isEmpty())
			return;
		
		workThread.sendMessage(constent);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { // 这里是鼠标双击进入房间
		MyJList listTeam = (MyJList) e.getSource();// 团队列表
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
			Player player = workThread.getRoom().findPlayerByIndex(teamType, index);
			if (player == null) {// Change team
				workThread.changeTeam(teamType, index);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		if (button == 3) {// right button
			popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object instanceof JButton) {
			JButton button = (JButton) object;
			if (button == btnStart) {
				// 开始游戏
				boolean seedSend = workThread.sendSeed();
				if (seedSend) // 种子发送
					btnStart.setEnabled(false);// 启动为假
			} else if (button == btnSend) {
				sendContentToServer();
			} else if (button == btnChoosePlayer) {
				ChooseCharacterDialog dialog = new ChooseCharacterDialog();
				dialog.setLocationRelativeTo(btnStart);
				dialog.setVisible(true);// 如果按钮不为空就启动选人界面
				int role_id = dialog.getRoleId();
				workThread.sendRoleChanged(role_id);
			}
		} else if (object instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) e.getSource();
			MyJList list = (MyJList) popupMenu.getInvoker();
			Team.Type teamType;
			int index = list.getSelectedIndex();
			if (list == listRed)
				teamType = Team.Type.RED;
			else
				teamType = Team.Type.BLUE;
			if (menuItem == menuKick) { // 如果点击的是踢人就发送
				workThread.sendKickCmd(teamType, index);// 调用工作线程将要踢掉的人发送出去
			} else if (menuItem == menuSetOwner) {
				workThread.sendTransistOwner(teamType, index);
			}
		}
	}

	@Override
	public void onSeed(long seed) { // 这个地方通知服务器启动游戏
		System.out.println("Seed received, start game: " + seed);
		// TODO 自动生成的方法存根
		DemoFrame gameFrame = new DemoFrame();
		gameFrame.setSeed(seed);
		gameFrame.setVisible(true);
	}

	@Override
	public void onPlayerKicked(Player player, Room room) {
		System.out.println(player.getName() + " is kicked：");
		roomRefreshed(room);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		workThread.close();
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOwerReset(int newOwner, Room room) {
		// TODO Auto-generated method stub
		Player player = room.findPlayerById(newOwner);// 把房主传到玩家中然后刷新出来
		if (player == null)
			return;

		roomRefreshed(room);
		addMsg(player.getName() + " 成为新的房主.");// 界面输入
	}

	@Override
	public void onPlayerEnter(Player player, Room room) {// 在接口中传入需要的Player room
		if (room == null) // 如果房间为空就返回空
			return;

		roomRefreshed(room);// 刷新房间信息
		addMsg(player.getName() + " 玩家加入战斗");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendContentToServer();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
