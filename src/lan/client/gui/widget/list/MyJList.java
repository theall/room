package lan.client.gui.widget.list;

import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.SwingUtilities;

public class MyJList extends JList<Object> {
    private static final long serialVersionUID = 1L;
    
    public MyJList() { //用函数封装了Jlist列表
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
    
    public void setListData(ArrayList<IconText> iconList) {
    	SwingUtilities.invokeLater(new Runnable() {  
            public void run() {  
            	ImageListModel model = new ImageListModel();
                for(IconText icon : iconList) {
                    model.addElement(icon);
                }
                setModel(model); //容器
            }  
        });
    }

    public static void main(String[] args) {
       // new MyJList().setVisible(true);
    }
}