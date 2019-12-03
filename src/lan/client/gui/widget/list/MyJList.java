package lan.client.gui.widget.list;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Vector;

public class MyJList extends JList {
    private static final long serialVersionUID = 1L;
    private ImageListModel listModel = new ImageListModel();
    public MyJList() {
        setModel(listModel);
        setCellRenderer(new ImageCellRender());
    }

    public void setListData(ArrayList<Integer> iconList, ArrayList<String> textList) {
        ArrayList<IconText> iconTextList = new ArrayList<>();
        for(int i=0;i<iconList.size();i++) {
            int icon = iconList.get(i);
            String text = textList.get(i);
            IconText iconText = new IconText(icon, text);
            iconTextList.add(iconText);
        }
        setListData(iconTextList);
    }

    public void setListData(ArrayList<Integer> iconList, Vector<String> textList) {

    }

    public void setListData(ArrayList<IconText> iconList) {
        for(IconText icon : iconList) {
            listModel.addElement(icon);
        }
        setModel(listModel);
    }

    public static void main(String[] args) {
       // new MyJList().setVisible(true);
    }
}