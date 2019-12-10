package lan.client.gui;

import lan.client.game.DemoFrame;
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

public class RoomDetail extends JFrame implements
        ClientInterface,
        MouseListener,
        ActionListener { // 战斗界面
    private Point mousePos = new Point();// 定义鼠标点击
    private ObjectOutputStream outputStream; // 输出流
    private JButton btnJoin;
    BufferedReader bReader;
    LinkedList<String> msgList = new LinkedList<String>();

    private static final long serialVersionUID = 1L;
    // 这里我设置的绘制区域需要调用的函数名
    JPanel panel;
    JScrollPane sPane;
    JTextArea txtContent;
    JLabel lblName, lblSend;//文本标签
    JTextField txtName, txtSend;
    JButton btnSend;
    JButton btnStart;
    JPopupMenu popupMenu;
    JMenuItem menuKick;
    JMenuItem menuSetOwner;

    private JButton btnChoosePlayer;//定义选人按钮
    PrintWriter pWriter;
    private MyJList listBlue; // 定义房间界面变量方便后面调用
    private MyJList listRed;
    private WorkThread workThread; // 工作线程

    public RoomDetail(RoomHeadInfo roomHeadInfo, String name) { // 客户端图形界面
        workThread = new WorkThread(roomHeadInfo, name);
        workThread.setClientInterface(this);
        workThread.start();

        setTitle("战斗房间"); // 标题
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(680, 460);
        setLocation(400, 240);
        setVisible(true);

        listBlue = createList();
        listRed = createList();

        txtContent = new JTextArea();
        // 设置文本域只读
        txtContent.setEditable(false);// 设置可编辑为假
        lblSend = new JLabel("发言:"); // 发言文本
        txtSend = new JTextField(20);// 文本框
        btnSend = new JButton("发送");// 按钮
        btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
        btnStart = new JButton("开始游戏");
        lblName = new JLabel("队伍列表");//创建了一个文本标签
        btnChoosePlayer = new JButton("选择角色");

        panel = new JPanel(); // new绘制对象出来
        panel.add(lblSend); // 进行我画的按钮，窗口，一切绘制出来
        panel.add(txtSend);
        panel.add(btnSend);
        panel.add(btnStart);// 绘制一个开始按钮
        panel.add(lblName);//绘制头顶列表信息
        panel.add(btnChoosePlayer);

        this.add(panel, BorderLayout.SOUTH); // 对话框跟队伍的位置，南边 BorderLayout边框布局
        this.add(listBlue, BorderLayout.WEST);// 队伍在左边函数 ，WEST西边
        this.add(listRed, BorderLayout.EAST);// 东边
        this.add(lblName, BorderLayout.NORTH);//北边，也就是置顶
        add(txtContent, BorderLayout.CENTER); // 中心

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 添加关闭程序
        loadIcons();

        btnSend.addActionListener(this);
        btnStart.addActionListener(this);
        btnChoosePlayer.addActionListener(this);
        listBlue.addMouseListener(this);
        listRed.addMouseListener(this);
        initPopMenu();
    }

    private MyJList createList() {//把列表封装成函数然后调用
        MyJList list = new MyJList();
        list.addMouseListener(this);
        Object[] Red = {"1号", "2号", "3号", "4号", "5号"};
        list.setListData(Red);
        add(list);
        list.setSize(100, 200);
        return list;
    }

    private void initPopMenu() {
        popupMenu = new JPopupMenu();//在方法中拿到JPopupMenu类的对象
        menuKick = new JMenuItem("踢出");//菜单列表
        menuSetOwner = new JMenuItem("房主");//菜单列表
        popupMenu.add(menuKick);//跟jPanel一样先注册在绘制
        popupMenu.addSeparator();//分割线
        popupMenu.add(menuSetOwner);
        setVisible(true);
        menuKick.addActionListener(this);
        menuSetOwner.addActionListener(this);
    }

    private void loadIcons() {
        File[] files = new File("src/resources").listFiles(new FileFilter() {
            public boolean accept(File file) {      //如果跳这里异常，不是文件路径错了，就是文件丢失
                return file.getName().endsWith("jpg");
            }
        });
        ArrayList<ImageIcon> iconArrayList = new ArrayList<>();
        for (File file : files) {
            ImageIcon icon = new ImageIcon(file.getPath());
            iconArrayList.add(icon);
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

        setTeamToList(room.getBlue(), listBlue); // 调用方法同步名字
        setTeamToList(room.getRed(), listRed); // 房间名字同步到了列表中
    }

    private void setTeamToList(Team team, MyJList list) { // 封装函数
        ArrayList<String> nameList = new ArrayList<String>();// 列表定义变量 vector
        ArrayList<Integer> idList = new ArrayList<>();
        for (int i = 0; i < team.getCapacity(); i++) { // 在所有的Team中循环
            Player player = team.getPlayer(i);// 这里是给player值等于索引i

            if (player == null) { // 如果列表玩家为空
                nameList.add("------------------");// 打印空字符串
            } else {
                nameList.add(player.getName());// 否则就输出名字
            }
            idList.add(1);
        }
        list.setListData(idList, nameList);
    }

    private void addMsg(String msg) {
        txtContent.append(msg); // 输出说的话
        txtContent.append("\n");// 换行
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
        int button = e.getButton();
        if(button == 3) {// right button
            popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
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
        if(object instanceof JButton) {
            try {
                JButton button = (JButton)object;
                if (button == btnStart) {
                    // 开始游戏
                    boolean seedSend = workThread.sendSeed();
                    if (seedSend)  //种子发送
                        btnStart.setEnabled(false);//启动为假
                } else if (button == btnSend) {
                    String constent = txtSend.getText();
                    workThread.sendMessage(constent); // 这里是接受消息
                } else if (button == btnChoosePlayer) {
                    new ChooseCharacterDialog().setVisible(true);//如果按钮不为空就启动选人界面
                }
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if(object instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();
            if (menuItem == menuKick) { //如果点击的是踢人就发送
                MyJList list = (MyJList) popupMenu.getInvoker();
                Team.Type teamType;
                int index;
                if (list == listRed)
                    teamType = Team.Type.RED;
                else
                    teamType = Team.Type.BLUE;

                workThread.sendKickCmd(teamType, list.getSelectedIndex());//调用工作线程将要踢掉的人发送出去
            }
        }
    }

    @Override
    public void onSeed(long seed) { //这个地方通知服务器启动游戏
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
}

