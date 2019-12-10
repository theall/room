package lan.client.gui.widget.list;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageListModel extends DefaultListModel {
    private static final long serialVersionUID = 1L;
    private List<IconText> iconTextList = new ArrayList<IconText>();

    public ImageListModel() {

    }

    public void addElement(IconText iconText) {
        iconTextList.add(iconText);
    }

    public void addElement(int icon, String text) {
        addElement(new IconText(icon, text));
    }

    public int getSize() {
        return iconTextList.size();
    }

    public IconText getElementAt(int index) {
        return iconTextList.get(index);
    }
}
