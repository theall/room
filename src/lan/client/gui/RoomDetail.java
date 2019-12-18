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
		implements ClientInterface, MouseListener, ActionListener, KeyListener, WindowListener { // ս������
	BufferedReader bReader;
	LinkedList<String> msgList = new LinkedList<String>();

    private static final long serialVersionUID = 1L;
    // ���������õĻ���������Ҫ���õĺ�����
    private JPanel panel;
    private JTextArea txtContent;// ���ֿ�
    private JLabel lblName, lblSend;// �ı���ǩ
    private JTextField txtSend;
    private JButton btnSend;
    private JButton btnStart;
    private JPopupMenu popupMenu;
    private JMenuItem menuKick;
    private JMenuItem menuSetOwner;
    private JButton btnChoosePlayer;// ����ѡ�˰�ť
    private MyJList listBlue; // ���巿������������������
    private MyJList listRed;
	private JScrollPane scrollPanel;

    private int roomOwnerId = -1;
    private WorkThread workThread; // �����߳�

    public RoomDetail(String host, int port, String name, boolean imOwner) { // �ͻ���ͼ�ν���
    	initialize();
    	btnStart.setEnabled(!imOwner);
    	
    	setButtonStartText(imOwner);
        setPopupMenuStatus(imOwner);

		workThread = new WorkThread(host, port, name);
		workThread.setClientInterface(this);
		workThread.start();
    }

    public void initialize() {
    	setTitle("ս������"); // ����
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//�ر�
        setSize(680, 460);
        setLocation(400, 240);
        listBlue = createList();
        listRed = createList();

		scrollPanel = new JScrollPane(txtContent);
        txtContent = new JTextArea(20, 50);// �����С
        // �����ı���ֻ��
        txtContent.setEditable(false);// ���ÿɱ༭Ϊ��
        lblSend = new JLabel("����:"); // �����ı�
        txtSend = new JTextField(20);// �ı���
        btnSend = new JButton("����");// ��ť
        btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);
        btnStart = new JButton();
        lblName = new JLabel("�����б�");// ������һ���ı���ǩ
        btnChoosePlayer = new JButton("ѡ���ɫ");

        panel = new JPanel(); // new���ƶ������
        panel.add(lblSend); // �����һ��İ�ť�����ڣ�һ�л��Ƴ���
        panel.add(txtSend);
        panel.add(btnSend);
        panel.add(btnStart);// ����һ����ʼ��ť
        panel.add(lblName);// ����ͷ���б���Ϣ
        panel.add(btnChoosePlayer);
		panel.add(scrollPanel);

        this.add(panel, BorderLayout.SOUTH); // �Ի���������λ�ã��ϱ� BorderLayout�߿򲼾�
        this.add(listBlue, BorderLayout.WEST);// ��������ߺ��� ��WEST����
        this.add(listRed, BorderLayout.EAST);// ����
        this.add(lblName, BorderLayout.NORTH);// ���ߣ�Ҳ�����ö�
		this.add(scrollPanel, BorderLayout.CENTER);// scrollPane����һ������
		this.scrollPanel.setViewportView(txtContent);// ��ʾ�����е��ı���
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
        popupMenu = new JPopupMenu();// �ڷ������õ�JPopupMenu��Ķ���
        menuKick = new JMenuItem("�߳�");// �˵��б�
        menuSetOwner = new JMenuItem("ת�Ʒ���");// �˵��б�
        popupMenu.add(menuKick);// ��jPanelһ����ע���ڻ���
        popupMenu.addSeparator();// �ָ���
        popupMenu.add(menuSetOwner);
        menuKick.addActionListener(this);
        menuSetOwner.addActionListener(this);
    }

    private void loadIcons() {
    	ArrayList<ImageIcon> iconArrayList = new ArrayList<>();
    	for(int i = 1; i <= 10; i++) {
    		String imageName = String.format("/resources/%d.jpg", i);
    		URL imageURL = this.getClass().getResource(imageName);
    		ImageIcon imageIcon = new ImageIcon(imageURL);
    		iconArrayList.add(imageIcon);
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

        roomOwnerId = room.getOwner();//�������ڷ������õ��˷�����ID
        updateRoomInfoToGui();
    }

    private void setTeamToList(Team team, MyJList list) { // ��װ����
        int myId = workThread.getMyId();
        ArrayList<IconText> iconTexts = new ArrayList<IconText>();
        for (int i = 0; i < team.getCapacity(); i++) { // �����е�Team��ѭ��
            Player player = team.getPlayer(i);// �����Ǹ�playerֵ��������i
            IconText iconText = new IconText();
            
            if (player != null) { // ����б����Ϊ��
            	String readyStr = player.isReady()?"(Ready)":"";
                iconText.setText(player.getName() + readyStr);
                iconText.setIcon(player.getRoleId());
                
                int playerId = player.getId();
                // Check if it's me
                if(playerId == roomOwnerId) {
                	iconText.setColor(Color.red);
                } else if(playerId == myId) {
                	iconText.setColor(Color.green);
                } 
            }
            iconTexts.add(iconText);
        }
        list.setListData(iconTexts);
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
        	Player player = workThread.getRoom().findPlayerByIndex(teamType, index);
        	if(player == null) {// Change team
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
        if(button == 3) {// right button
            popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
        }
    }

	private void sendContentToServer() {
		String constent = txtSend.getText().trim();
		txtSend.setText("");
		if(constent.isEmpty())
			return;
		
		workThread.sendMessage(constent);
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
				Player me = workThread.getMe();
				workThread.sendReadyCmd(!me.isReady());
			} else if (button == btnSend) {
				sendContentToServer();
			} else if (button == btnChoosePlayer) {
				ChooseCharacterDialog dialog = new ChooseCharacterDialog();
				dialog.setLocationRelativeTo(btnStart);
				dialog.setVisible(true);// �����ť��Ϊ�վ�����ѡ�˽���
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
            if (menuItem == menuKick) { // �������������˾ͷ���
                workThread.sendKickCmd(teamType, index);// ���ù����߳̽�Ҫ�ߵ����˷��ͳ�ȥ
            } else if (menuItem == menuSetOwner) {
            	workThread.sendTransistOwner(teamType, index);
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
		Player player = room.findPlayerById(newOwner);// �ѷ������������Ȼ��ˢ�³���
		if (player == null)
			return;
        setPopupMenuStatus(workThread.imOwner());//���ú��������ж�ˢ��
        setButtonStartText(workThread.imOwner());//���ú��������ж�ˢ��
		roomRefreshed(room);
		addMsg(player.getName() + " ��Ϊ�µķ���.");// ��������
	}

    @Override
	public void onPlayerEnter(Player player, Room room) {// �ڽӿ��д�����Ҫ��Player room
		if (room == null) // �������Ϊ�վͷ��ؿ�
			return;

		roomRefreshed(room);// ˢ�·�����Ϣ
		addMsg(player.getName() + " ��Ҽ���ս��");
	}

    private void setButtonStartText(boolean isOwner) {
        if(isOwner)
            btnStart.setText("��ʼ��Ϸ");
        else
            btnStart.setText("׼����Ϸ");

    }
    private void setPopupMenuStatus(boolean status) {
        if(status){
            menuKick.setEnabled(true);
            menuSetOwner.setEnabled(true);
        }
        else {
            menuKick.setEnabled(false);
            menuSetOwner.setEnabled(false);
        }

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

	@Override
	public void onPlayerReadyStateChanged(int playerId, boolean isReady) {
		Room room = workThread.getRoom();
		if(room == null)
			return;
		
		Player player = room.findPlayerById(playerId);
        if(player == null)
        	return;
        
        player.setReady(isReady);
        updateRoomInfoToGui();
        
        boolean isMe = playerId == workThread.getMyId();
        if(isMe)
        	btnStart.setText(isReady?"Cancel":"Ready");
        
        if(workThread.imOwner())
        	btnStart.setEnabled(room.isAllReady());
	}
	
	private void updateRoomInfoToGui() {
		Room room = workThread.getRoom();
		setTeamToList(room.getBlue(), listBlue); // ���÷���ͬ������
        setTeamToList(room.getRed(), listRed); // ��������ͬ�������б���
	}
}

