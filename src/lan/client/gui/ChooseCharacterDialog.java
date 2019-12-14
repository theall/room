package lan.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import lan.client.gui.widget.list.ImageCellRender;
import lan.client.gui.widget.list.MyJList;

public class ChooseCharacterDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyJList list = new MyJList();//���ҵ�MYJList�д�����LISt
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    private int index;
    public ChooseCharacterDialog() {
    	setModal(true);
    	
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
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//�رպ���
        setBounds(900, 350, 150, 300);
        list.addMouseListener(new MouseListener() { //��������Ҳ�������㵽�ҵ�ͼƬ�б�Ϳ�ʼ����
            @Override
            public void mouseClicked(MouseEvent e) {
                index = list.getSelectedIndex();
                if (index != -1) {                  //���������������1��
                	setVisible(false);//�������ξ�ֱ���˳�
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
    
    public int getRoleId() {
		return index;
	}
}
