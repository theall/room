package lan.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import lan.client.gui.widget.list.ImageCellRender;
import lan.client.gui.widget.list.MyJList;

public class ChooseCharacterDialog extends JFrame {
    private MyJList list = new MyJList();

    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    public ChooseCharacterDialog() {
        for (int i = 0; i< ImageCellRender.getCharacterSize(); i++) {
            idList.add(i);
            nameList.add(String.valueOf(i));
        }
        list.setListData(idList, nameList);

        //将JList放在滚动面板上
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane);
        getContentPane().add(scrollPane, BorderLayout.CENTER);//中心
        getContentPane().add(new JLabel("人物列表"), BorderLayout.NORTH);//位子在北面
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭函数
        setBounds(900, 350, 150, 300);
        list.addMouseListener(new MouseListener() { //鼠标监听，也就是鼠标点到我的图片列表就开始监听
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.getSelectedIndex();
                if (index != -1) {                  //如果索引到不少于1次
                    int clickCount = e.getClickCount();//点击计数
                    if (clickCount == 2) {              //如果2次就进入
                        setVisible(false);//点了俩次就直接退出
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

    public static void main(String[] args) {
        new ChooseCharacterDialog().setVisible(true);//启动选人列表
    }
}
