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

public class RoomList extends JFrame implements RoomUpdate { //房间界面
    private JLabel lblName; //定义按钮变量
    private JList list; //定义房间界面变量方便后面调用
    JTextField txtName;//构建我需要的框架 JTextField这个类表示文本框，只能是单行文本不能多行文本
    private JButton btnSend;//创建按钮对象
    private FindHostThread findThread;

    public RoomList() { //构造房间界面的方法
        findThread = new FindHostThread();//这里线程是如果没有房间就为空，有房间就接受
        findThread.setRoomUpdate(this);
        findThread.start();
        char cha[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";//定义字符串变量
        for (int i = 0; i < 3; i++) {//进行循环
            int index;
            index = (int) (Math.random() * (cha.length)); //随机在A到Z3个字母
            str = str + String.valueOf(cha[index]);
        }


        list = new JList();//new出对象
        lblName = new JLabel("昵称:");
        txtName = new JTextField(str, 5);//这里用法是一个长度为int5的文本框 //随机名字

        JPanel panel;

        Object[] data = {"1号房间", "2号房间", "3号房间"};//数组类型的房间
        list.setListData(data);//这里是实例化
        add(list);//绘制界面大小
        setTitle("房间列表");
        setSize(480, 360);
        setLocation(400, 240);

        panel = new JPanel(); //这里是在房间里添加我需要的按钮跟生成框架
        panel.add(lblName);
        panel.add(txtName);
        this.add(panel, BorderLayout.SOUTH);//这里是绘制的位置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //添加关闭动作

        RoomList self = this;
        list.addMouseListener(new MouseListener() { //鼠标监听，也就是鼠标点到我的列表就开始监听
            @Override
            public void mouseClicked(MouseEvent e) { //这里是鼠标双击进入房间
                int index = list.getSelectedIndex(); //定义了索引，list选择索引，也就是在list当中点点点
                if (index != -1) {                  //如果索引到不少于1次
                    int clickCount = e.getClickCount();//点击计数
                    if (clickCount == 2) {              //如果2次就进入
                        String name = txtName.getText();//点了俩次以后房间名字同步
                        if (name.trim().length() <= 0) { //这里用到了trim函数然后对比长度为0就无法
                            JOptionPane.showMessageDialog(self, "名字为空");//如果名字为空就无法进入
                            return;
                        } else {

                        }
                        RoomHeadInfo roomHeadInfo = findThread.getRoom(index); //查找线程
                        RoomDetail roomDetail = new RoomDetail(roomHeadInfo, name); //这里是在战斗界面给对象然后给参数
                        roomDetail.setVisible(true);//设置可见性
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


    void setRoom(RoomHeadList roomHeadList) { //定义函数给参数
        if (roomHeadList == null || list == null) //如果战斗界面为空与列表为空
            return;                 //什么都不返回
        //向量//字符串//房间名称=创建对象//
        Vector<String> roomNames = new Vector<String>();//这里是接口
        for (RoomHeadInfo roomHeadInfo : roomHeadList.getRoomHeadInfo()) {
            roomNames.add(roomHeadInfo.name);
        }
        list.setListData(roomNames); //数据列表
    }

    @Override
    public void updated(RoomHeadList roomHeadList) {//房间头列表
        setRoom(roomHeadList); //调用方法然后显示列表
    }
}
