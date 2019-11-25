package lan.client.game;

import javax.swing.*;
import java.awt.*;

public class Demo {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private int width;
    private int height;
    private Image image;
    public Demo(){
        this.image=new ImageIcon("src/resources/0b9701b0089543eba52b207af41cec63_th.png").getImage();
        this.width=100;
        this.height=100;
    }
}
