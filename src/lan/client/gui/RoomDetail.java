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

public class RoomDetail extends JFrame implements ClientInterface { //战斗界面
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
                ////注册监听
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
    //这里我设置的绘制区域需要调用的函数名
    JPanel panel;
    JScrollPane sPane;
    JTextArea txtContent;
    JLabel lblName,lblSend;
    JTextField txtName,txtSend;
    JButton btnSend;

    private JList list; //定义房间界面变量方便后面调用
    private JList list2;
    private WorkThread workThread;
    public RoomDetail (RoomHeadInfo roomHeadInfo, String name) { //客户端图形界面
        workThread = new WorkThread(roomHeadInfo, name);
        workThread.setClientInterface(this);
        workThread.start();

        myPanel = new MyPanel();
        list = new JList();//new出对象
        Object[] BLUE = {"1号", "2号", "3号","4号","5号"};
        //"=====","6号","7号","8号","9号","10号"};//数组类型的队列
        list.setListData(BLUE);//这里是实例化
        list.setBackground( Color.BLUE);
              //无法共存
        list2 = new JList();
        Object[] Red = {"6号", "7号", "8号","9号","10号"};
        list2.setListData( Red);
        add(list2);
        list2.setSize(100,200);
        list2.setBackground( Color.RED);//这里是红蓝队伍颜色
        add(list);//绘制界面大小
        list.setSize(100, 200);
        setSize(480, 360);
        setLocation(400, 240);

        setTitle("战斗房间"); //标题

        txtContent = new JTextArea();
        //设置文本域只读
        txtContent.setEditable(false);
        //txtContent.append(list + "\n");

//        lblName = new JLabel("昵称:");
//        txtName = new JTextField(5);
        lblSend = new JLabel("发言:");
        txtSend = new JTextField(20);
        btnSend = new JButton("发送");
        btnSend.setMnemonic(java.awt.event.KeyEvent.VK_ENTER);

        panel = new JPanel();
//        panel.add(lblName);
//        panel.add(txtName);
        panel.add(lblSend);
        panel.add(txtSend);
        panel.add(btnSend);


        this.add(panel,BorderLayout.SOUTH); //对话框跟队伍的位置，中心 BorderLayout边框布局
        this.add(list, BorderLayout.WEST);//队伍在左边函数 ，WEST西边
        this.add(list2,BorderLayout.EAST);//北边
        add(txtContent,BorderLayout.CENTER); //南边
        //this.setSize(500,300); //这里是我写的长宽
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
                    workThread.sendMessage(constent); //这里是接受消息
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessage(String player, String msg) {//上面的消息
        txtContent.append(msg);
        txtSend.setText(""); //清空对话框消息
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
