package lan.client.gui;

import lan.client.Client;
import lan.client.thread.FindHostThread;
import lan.client.thread.RoomHeadInfo;
import lan.client.thread.RoomHeadList;
import lan.client.util.RoomUpdate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class RoomList extends JFrame implements RoomUpdate { //�������
    private JLabel lblName; //���尴ť����
    private JList list; //���巿������������������
    JTextField txtName;//��������Ҫ�Ŀ�� JTextField������ʾ�ı���ֻ���ǵ����ı����ܶ����ı�
    private JButton btnSend;//������ť����
    private FindHostThread findThread;

    public RoomList() { //���췿�����ķ���
        findThread = new FindHostThread();//�����߳������û�з����Ϊ�գ��з���ͽ���
        findThread.setRoomUpdate(this);
        findThread.start();
        char cha[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";//�����ַ�������
        for (int i = 0; i < 3; i++) {//����ѭ��
            int index;
            index = (int) (Math.random() * (cha.length)); //�����A��Z3����ĸ
            str = str + String.valueOf(cha[index]);
        }


        list = new JList();//new������
        lblName = new JLabel("�ǳ�:");
        txtName = new JTextField(str, 5);//�����÷���һ������Ϊint5���ı��� //�������

        JPanel panel;

        Object[] data = {"1�ŷ���", "2�ŷ���", "3�ŷ���"};//�������͵ķ���
        list.setListData(data);//������ʵ����
        add(list);//���ƽ����С
        setTitle("�����б�");
        setSize(480, 360);
        setLocation(400, 240);

        panel = new JPanel(); //�������ڷ������������Ҫ�İ�ť�����ɿ��
        panel.add(lblName);
        panel.add(txtName);
        this.add(panel, BorderLayout.SOUTH);//�����ǻ��Ƶ�λ��
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //��ӹرն���

        RoomList self = this;
        list.addMouseListener(new MouseListener() { //��������Ҳ�������㵽�ҵ��б�Ϳ�ʼ����
            @Override
            public void mouseClicked(MouseEvent e) { //���������˫�����뷿��
                int index = list.getSelectedIndex(); //������������listѡ��������Ҳ������list���е���
                if (index != -1) {                  //���������������1��
                    int clickCount = e.getClickCount();//�������
                    if (clickCount == 2) {              //���2�ξͽ���
                        String name = txtName.getText();//���������Ժ󷿼�����ͬ��
                        if (name.trim().length() <= 0) { //�����õ���trim����Ȼ��Աȳ���Ϊ0���޷�
                            JOptionPane.showMessageDialog(self, "����Ϊ��");//�������Ϊ�վ��޷�����
                            return;
                        } else {

                        }
                        RoomHeadInfo roomHeadInfo = findThread.getRoom(index); //�����߳�
                        RoomDetail roomDetail = new RoomDetail(roomHeadInfo, name); //��������ս�����������Ȼ�������
                        roomDetail.setVisible(true);//���ÿɼ���
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
        });
    }


    void setRoom(RoomHeadList roomHeadList) { //���庯��������
        if (roomHeadList == null || list == null) //���ս������Ϊ�����б�Ϊ��
            return;                 //ʲô��������
        //����//�ַ���//��������=��������//
        Vector<String> roomNames = new Vector<String>();//�����ǽӿ�
        for (RoomHeadInfo roomHeadInfo : roomHeadList.getRoomHeadInfo()) {
            roomNames.add(roomHeadInfo.name);
        }
        list.setListData(roomNames); //�����б�
    }

    @Override
    public void updated(RoomHeadList roomHeadList) {//����ͷ�б�
        setRoom(roomHeadList); //���÷���Ȼ����ʾ�б�
    }
}
