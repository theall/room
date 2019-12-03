package lan.client.gui.widget.list;

import javax.swing.*;

public class IconText {
    private int icon;
    private String text;

    public IconText(int icon) {
        this.icon = icon;
    }

    public IconText(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
