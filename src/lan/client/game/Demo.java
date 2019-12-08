package lan.client.game;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Demo {
	private int x;
	private int y;
	private boolean upIsDown;

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

	public Demo() {
		//URL imgURL = Demo.class.getResource("resources/test.jpg");
		//this.image =  new ImageIcon("scr/resources/1.jpg").getImage();
		this.image =new ImageIcon("src/resources/1.jpg").getImage();
		this.width = 100;
		this.height = 100;
	}

	public void step() {
		if(upIsDown) {

		}
	}

	public void render(Graphics g) {

	}
}
