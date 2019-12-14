package lan.client.gui.widget.list;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

public class ImageCellRender extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;
    private static ArrayList<ImageIcon> characters;

    public static int getCharacterSize() {
        return characters!=null?characters.size():0;
    }

    public static void setCharacters(ArrayList<ImageIcon> characters) {
        ImageCellRender.characters = characters;
    }

    public static ImageIcon getIcon(int id) {
        if(characters == null)
            return null;
        if(id<0 || id>=characters.size())
            return null;
        return characters.get(id);
    }
    
    public Component getListCellRendererComponent(JList<? extends Object> list,
                                                  Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if(value instanceof IconText) { //循环图标文本
            IconText iconText = (IconText)value;
            int id = iconText.getIcon();
            ImageIcon icon = getIcon(id);
            if(icon != null)
                setIcon(icon);
            setText(iconText.getText());
            setForeground(iconText.getColor());
        }
        return this;
    }
}
