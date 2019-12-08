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

        //��JList���ڹ��������
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane);
        getContentPane().add(scrollPane, BorderLayout.CENTER);//����
        getContentPane().add(new JLabel("�����б�"), BorderLayout.NORTH);//λ���ڱ���
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�رպ���
        setBounds(900, 350, 150, 300);
        list.addMouseListener(new MouseListener() { //��������Ҳ�������㵽�ҵ�ͼƬ�б�Ϳ�ʼ����
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.getSelectedIndex();
                if (index != -1) {                  //���������������1��
                    int clickCount = e.getClickCount();//�������
                    if (clickCount == 2) {              //���2�ξͽ���
                        setVisible(false);//�������ξ�ֱ���˳�
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
        new ChooseCharacterDialog().setVisible(true);//����ѡ���б�
    }
}
