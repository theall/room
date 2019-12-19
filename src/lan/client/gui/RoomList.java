package lan.client.gui;

import lan.server.Server;
import lan.utils.Room;
import lan.utils.Utils;
import lan.client.thread.FindHostThread;
import lan.client.thread.RoomHeadInfo;
import lan.client.thread.RoomHeadList;
import lan.client.util.RoomUpdate;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RoomList extends JFrame implements RoomUpdate, ActionListener, MouseListener, WindowListener { /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    //房间界面
    private JLabel lblName; //定义按钮变量
	private JList<String> listRooms; //定义房间界面变量方便后面调用
    private JTextField txtName;//构建我需要的框架 JTextField这个类表示文本框，只能是单行文本不能多行文本
    private FindHostThread findThread;
    private JButton btnCreateRoom;
    private Server localServer;
    
    public RoomList() { //构造房间界面的方法   	
        findThread = new FindHostThread();//这里线程是如果没有房间就为空，有房间就接受
        findThread.setRoomUpdate(this);
        findThread.start();

        setTitle("房间列表");
        setSize(480, 360);
        setLocation(400, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //添加关闭动作
        
        listRooms = new JList<String>();//new出对象
        listRooms.addMouseListener(this);
        lblName = new JLabel("昵称:");
        txtName = new JTextField(getRandomName(), 5);//这里用法是一个长度为int5的文本框 //随机名字
        btnCreateRoom = new JButton();//创建房间按钮
        btnCreateRoom.setText("Create");
        btnCreateRoom.addActionListener(this);
        
        JPanel panel = new JPanel(); //这里是在房间里添加我需要的按钮跟生成框架
        panel.add(lblName);
        panel.add(txtName);
        panel.add(btnCreateRoom);

        add(panel, BorderLayout.SOUTH);//这里是绘制的位置
        add(listRooms);//绘制界面大小
    }

    private String getRandomName() {
    	char cha[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";//定义字符串变量
        for (int i = 0; i < 5; i++) {//进行循环
            int index;
            index = (int) (Math.random() * (cha.length)); //随机在A到Z3个字母
            str = str + String.valueOf(cha[index]);
        }
        return str;
    }

    void setRoom(RoomHeadList roomHeadList) { //定义函数给参数
        if (roomHeadList == null || listRooms == null) //如果战斗界面为空与列表为空
            return;                 //什么都不返回
        
        //向量//字符串//房间名称=创建对象//
        Vector<String> roomNames = new Vector<String>();//这里是接口
        for (RoomHeadInfo roomHeadInfo : roomHeadList.getRoomHeadInfo()) {
            roomNames.add(roomHeadInfo.name);
        }
        listRooms.setListData(roomNames); //数据列表
    }

    @Override
    public void updated(RoomHeadList roomHeadList) {//房间头列表
        setRoom(roomHeadList); //调用方法然后显示列表
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnCreateRoom) {			
			// Create room
			String name = txtName.getText();
			localServer = new Server();
			Room room = localServer.createRoom(name);
			localServer.start();
			localServer.broadcastRoom(room);
			
			String serverHost = localServer.getHost();
			setVisible(false);
			RoomDetail roomDetail = new RoomDetail(serverHost, Utils.WORK_PORT, name, true); //这里是在战斗界面给对象然后给参数
			roomDetail.setVisible(true);
            localServer.shutdown();
            setVisible(true);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {//双击进入房间
		int index = listRooms.getSelectedIndex(); //定义了索引，list选择索引，也就是在list当中点点点
        if (index != -1) {                  //如果索引到不少于1次
            String name = txtName.getText();//点了俩次以后房间名字同步
            if (name.trim().length() <= 0) { //这里用到了trim函数然后对比长度为0就无法
                JOptionPane.showMessageDialog(this, "名字为空");//如果名字为空就无法进入
                return;
            }
            RoomHeadInfo roomHeadInfo = findThread.getRoom(index); //查找线程
            setVisible(false);
            RoomDetail roomDetail = new RoomDetail(roomHeadInfo.host, roomHeadInfo.port, name, false); //这里是在战斗界面给对象然后给参数
            roomDetail.setVisible(true);
            setVisible(true);
        }
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
}
