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
    //�������
    private JLabel lblName; //���尴ť����
	private JList<String> listRooms; //���巿������������������
    private JTextField txtName;//��������Ҫ�Ŀ�� JTextField������ʾ�ı���ֻ���ǵ����ı����ܶ����ı�
    private FindHostThread findThread;
    private JButton btnCreateRoom;
    private Server localServer;
    
    public RoomList() { //���췿�����ķ���   	
        findThread = new FindHostThread();//�����߳������û�з����Ϊ�գ��з���ͽ���
        findThread.setRoomUpdate(this);
        findThread.start();

        setTitle("�����б�");
        setSize(480, 360);
        setLocation(400, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //��ӹرն���
        
        listRooms = new JList<String>();//new������
        listRooms.addMouseListener(this);
        lblName = new JLabel("�ǳ�:");
        txtName = new JTextField(getRandomName(), 5);//�����÷���һ������Ϊint5���ı��� //�������
        btnCreateRoom = new JButton();//�������䰴ť
        btnCreateRoom.setText("Create");
        btnCreateRoom.addActionListener(this);
        
        JPanel panel = new JPanel(); //�������ڷ������������Ҫ�İ�ť�����ɿ��
        panel.add(lblName);
        panel.add(txtName);
        panel.add(btnCreateRoom);

        add(panel, BorderLayout.SOUTH);//�����ǻ��Ƶ�λ��
        add(listRooms);//���ƽ����С
    }

    private String getRandomName() {
    	char cha[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";//�����ַ�������
        for (int i = 0; i < 5; i++) {//����ѭ��
            int index;
            index = (int) (Math.random() * (cha.length)); //�����A��Z3����ĸ
            str = str + String.valueOf(cha[index]);
        }
        return str;
    }

    void setRoom(RoomHeadList roomHeadList) { //���庯��������
        if (roomHeadList == null || listRooms == null) //���ս������Ϊ�����б�Ϊ��
            return;                 //ʲô��������
        
        //����//�ַ���//��������=��������//
        Vector<String> roomNames = new Vector<String>();//�����ǽӿ�
        for (RoomHeadInfo roomHeadInfo : roomHeadList.getRoomHeadInfo()) {
            roomNames.add(roomHeadInfo.name);
        }
        listRooms.setListData(roomNames); //�����б�
    }

    @Override
    public void updated(RoomHeadList roomHeadList) {//����ͷ�б�
        setRoom(roomHeadList); //���÷���Ȼ����ʾ�б�
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
			RoomDetail roomDetail = new RoomDetail(serverHost, Utils.WORK_PORT, name, true); //��������ս�����������Ȼ�������
			roomDetail.setVisible(true);
            localServer.shutdown();
            setVisible(true);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {//˫�����뷿��
		int index = listRooms.getSelectedIndex(); //������������listѡ��������Ҳ������list���е���
        if (index != -1) {                  //���������������1��
            String name = txtName.getText();//���������Ժ󷿼�����ͬ��
            if (name.trim().length() <= 0) { //�����õ���trim����Ȼ��Աȳ���Ϊ0���޷�
                JOptionPane.showMessageDialog(this, "����Ϊ��");//�������Ϊ�վ��޷�����
                return;
            }
            RoomHeadInfo roomHeadInfo = findThread.getRoom(index); //�����߳�
            setVisible(false);
            RoomDetail roomDetail = new RoomDetail(roomHeadInfo.host, roomHeadInfo.port, name, false); //��������ս�����������Ȼ�������
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
