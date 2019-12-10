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
        ActionListener { // ս������
    private Point mousePos = new Point();// ���������
    private ObjectOutputStream outputStream; // �����
    private JButton btnJoin;
    BufferedReader bReader;
    LinkedList<String> msgList = new LinkedList<String>();

    private static final long serialVersionUID = 1L;
    // ���������õĻ���������Ҫ���õĺ�����
    JPanel panel;
    JScrollPane sPane;
    JTextArea txtContent;
    JLabel lblName, lblSend;//�ı���ǩ
    JTextField txtName, txtSend;
    JButton btnSend;
    JButton btnStart;
    JPopupMenu popupMenu;
    JMenuItem menuKick;
    JMenuItem menuSetOwner;

    private JButton btnChoosePlayer;//����ѡ�˰�ť
    PrintWriter pWriter;
    private MyJList listBlue; // ���巿������������������
    private MyJList listRed;
    private WorkThread workThread; // �����߳�

    public RoomDetail(RoomHeadInfo roomHeadInfo, String name) { // �ͻ���ͼ�ν���
        workThread = new WorkThread(roomHeadInfo, name);
        workThread.setClientInterface(this);
        workThread.start();

        setTitle("ս������"); // ����
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(680, 460);
        setLocation(400, 240);
        setVisible(true);

        listBlue = createList();
        listRed = createList();

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
        this.add(lblName, BorderLayout.NORTH);//���ߣ�Ҳ�����ö�
        add(txtContent, BorderLayout.CENTER); // ����

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ��ӹرճ���
        loadIcons();

        btnSend.addActionListener(this);
        btnStart.addActionListener(this);
        btnChoosePlayer.addActionListener(this);
        listBlue.addMouseListener(this);
        listRed.addMouseListener(this);
        initPopMenu();
    }

    private MyJList createList() {//���б��װ�ɺ���Ȼ�����
        MyJList list = new MyJList();
        list.addMouseListener(this);
        Object[] Red = {"1��", "2��", "3��", "4��", "5��"};
        list.setListData(Red);
        add(list);
        list.setSize(100, 200);
        return list;
    }

    private void initPopMenu() {
        popupMenu = new JPopupMenu();//�ڷ������õ�JPopupMenu��Ķ���
        menuKick = new JMenuItem("�߳�");//�˵��б�
        menuSetOwner = new JMenuItem("����");//�˵��б�
        popupMenu.add(menuKick);//��jPanelһ����ע���ڻ���
        popupMenu.addSeparator();//�ָ���
        popupMenu.add(menuSetOwner);
        setVisible(true);
        menuKick.addActionListener(this);
        menuSetOwner.addActionListener(this);
    }

    private void loadIcons() {
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
                    // ��ʼ��Ϸ
                    boolean seedSend = workThread.sendSeed();
                    if (seedSend)  //���ӷ���
                        btnStart.setEnabled(false);//����Ϊ��
                } else if (button == btnSend) {
                    String constent = txtSend.getText();
                    workThread.sendMessage(constent); // �����ǽ�����Ϣ
                } else if (button == btnChoosePlayer) {
                    new ChooseCharacterDialog().setVisible(true);//�����ť��Ϊ�վ�����ѡ�˽���
                }
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if(object instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();
            if (menuItem == menuKick) { //�������������˾ͷ���
                MyJList list = (MyJList) popupMenu.getInvoker();
                Team.Type teamType;
                int index;
                if (list == listRed)
                    teamType = Team.Type.RED;
                else
                    teamType = Team.Type.BLUE;

                workThread.sendKickCmd(teamType, list.getSelectedIndex());//���ù����߳̽�Ҫ�ߵ����˷��ͳ�ȥ
            }
        }
    }

    @Override
    public void onSeed(long seed) { //����ط�֪ͨ������������Ϸ
        System.out.println("Seed received, start game: " + seed);
        // TODO �Զ����ɵķ������
        DemoFrame gameFrame = new DemoFrame();
        gameFrame.setSeed(seed);
        gameFrame.setVisible(true);
    }

    @Override
    public void onPlayerKicked(Player player, Room room) {
        System.out.println(player.getName() + " is kicked��");
        roomRefreshed(room);
    }
}

