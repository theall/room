package lan.client.gui.widget.list;

import java.awt.Color;

public class IconText { //封装图标和文本创建对象
    private int icon;
    private String text;
    private Color color;

    public IconText() {
        this.icon = 0;
        this.color = Color.black;
        this.text = "------------------";
    }
    
	public IconText(int icon) {
        this.icon = icon;
        this.text = "------------------";
        this.color = Color.black;
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

    public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
