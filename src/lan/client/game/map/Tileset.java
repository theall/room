package lan.client.game.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class Tileset {//������Ҫ��ͼ�ϴ�һ����ͼȻ���õ�����ŵ�����
    private int rows;
    private int cols;
    private Image[] images;
    private int tileWidth;
    private int tileHeight;
    public void load(String pngName, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        split(pngName);
    }

    private void split(String pngName) {
        URL url = Tileset.class.getClassLoader().getResource(pngName);
        BufferedImage tilesetImage = null;
        try {
            tilesetImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int imageWidth = tilesetImage.getWidth();
        int imageHeight = tilesetImage.getHeight();
        rows = imageHeight / tileHeight;
        cols = imageWidth / tileWidth;
        int chunks = rows * cols;

        // ����ÿ��Сͼ�Ŀ�Ⱥ͸߶�
        int chunkWidth = tilesetImage.getWidth() / cols;
        int chunkHeight = tilesetImage.getHeight() / rows;

        int count = 0;
        images = new BufferedImage[chunks];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //����Сͼ�Ĵ�С������
                BufferedImage image = new BufferedImage(chunkWidth, chunkHeight, tilesetImage.getType());

                //д��ͼ������
                Graphics2D gr = image.createGraphics();
                gr.drawImage(tilesetImage, 0, 0,
                        chunkWidth, chunkHeight,
                        chunkWidth * y, chunkHeight * x,
                        chunkWidth * y + chunkWidth,
                        chunkHeight * x + chunkHeight, null);
                gr.dispose();

                images[count++] = image;
            }
        }
    }

    public Image getImage(int row, int col) {
        return null;
    }

    public Image getImage(int id) {
        if(id<0 || id>=images.length)
            return null;
        return images[id];
    }
}
