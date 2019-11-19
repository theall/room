package lan.client.gui;

import lan.client.thread.RoomHeadInfo;
import lan.client.thread.WorkThread;
import lan.client.util.ClientInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class RoomDetail extends JFrame implements ClientInterface { //ս������
    private Point mousePos = new Point();
    private ObjectOutputStream outputStream;
    private RoomDetail.MyPanel myPanel;
    private JButton btnJoin;

    private class MyPanel extends JPanel {
        public MyPanel() {
            FlowLayout layout = new FlowLayout();
            setLayout(layout);
            JTextField txtName = new JTextField();
            //txtName.setText("client");
            JList listRoom = new JList();

            JPanel leftPanel = new JPanel();
            leftPanel.add(txtName);
            leftPanel.add(listRoom);

            btnJoin = new JButton();
            btnJoin.setText("join");
            listRoom.add(btnJoin);

            btnJoin.addActionListener(new ActionListener() {
                ////ע�����
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
    //���������õĻ���������Ҫ���õĺ�����
    JPanel panel;
    JScrollPane sPane;
    JTextArea txtContent;
    JLabel lblName,lblSend;
    JTextField txtName,txtSend;
    JButton btnSend;

    private JList list; //���巿������������������
    private JList list2;
    private WorkThread workThread;
    public RoomDetail (RoomHeadInfo roomHeadInfo, String name) { //�ͻ���ͼ�ν���
        workThread = new WorkThread(roomHeadInfo, name);
        workThread.setClientInterface(this);
        workThread.start();

        myPanel = new MyPanel();
        list = new JList();//new������
        Object[] BLUE = {"1��", "2��", "3��","4��","5��"};
        //"=====","6��","7��","8��","9��","10��"};//�������͵Ķ���
        list.setListData(BLUE);//������ʵ����
        list.setBackground( Color.BLUE);
              //�޷�����
        list2 = new JList();
        Object[] Red = {"6��", "7��", "8��","9��","10��"};
        list2.setListData( Red);
        add(list2);
        list2.setSize(100,200);
        list2.setBackground( Color.RED);//�����Ǻ���������ɫ
        add(list);//���ƽ����С
        list.setSize(100, 200);
        setSize(480, 360);
        setLocation(400, 240);

        setTitle("ս������"); //����

        txtContent = new JTextArea();
        //�����ı���ֻ��
        txtContent.setEditable(false);
        //txtContent.append(list + "\n");

//        lblName = new JLabel("�ǳ�:");
//        txtName = new JTextField(5);
        lblSend = new JLabel("����:");
        txtSend = new JTextField(20);
        btnSend = new JButton("����");
        btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);

        panel = new JPanel();
//        panel.add(lblName);
//        panel.add(txtName);
        panel.add(lblSend);
        panel.add(txtSend);
        panel.add(btnSend);


        this.add(panel,BorderLayout.SOUTH); //�Ի���������λ�ã����� BorderLayout�߿򲼾�
        this.add(list, BorderLayout.WEST);//��������ߺ��� ��WEST����
        this.add(list2,BorderLayout.EAST);//����
        add(txtContent,BorderLayout.CENTER); //�ϱ�
        //this.setSize(500,300); //��������д�ĳ���
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String constent = txtSend.getText();
                try {
                    workThread.sendMessage(constent); //�����ǽ�����Ϣ
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessage(String player, String msg) {//�������Ϣ
        txtContent.append(msg);
        txtSend.setText(""); //��նԻ�����Ϣ
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
