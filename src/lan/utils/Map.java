package lan.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Map {
	private String name;
	private int capacity;
	public Image image;

	public Map(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
		createTestImage();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Image getImage() {
		return image;
	}

	private void createTestImage() {
		image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

		// 2.创建绘制环境
		Graphics paint = image.getGraphics();
		Color c = new Color(200, 150, 255);
		// 设置画笔
		paint.setColor(c);
		// 画背景
		paint.fillRect(0, 0, 100, 100);

		// 绘制数字和字母
		StringBuffer codes = new StringBuffer();
		char[] ch = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890".toCharArray();
		Random r = new Random();
		int index;

		for (int i = 0; i < 4; i++) {
			index = r.nextInt(ch.length);
			// 设置文本颜色
			paint.setColor(new Color(r.nextInt(88), r.nextInt(150), r.nextInt(255)));
			paint.drawString(ch[index] + "", (i * 16) + 3, 18);
			codes.append(ch[index]);
		}
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
