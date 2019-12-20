package lan.client.game.sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Action {
    private Image[] images = new Image[4];
    private int index;
    private static int DURATION = 10;//����ʱ��
    private int frameSequence;

    public Action() {//�
        frameSequence = 0;
        index = 0;
    }
    public void load(BufferedImage image) {
        int chunkWidth = image.getWidth() / 4;
        int chunkHeight = image.getHeight();
        for (int x = 0; x < 4; x++) {
            //����Сͼ�Ĵ�С������
            BufferedImage bufferedImage = new BufferedImage(chunkWidth, chunkHeight, image.getType());

            //д��ͼ������
            Graphics2D gr = bufferedImage.createGraphics();
            gr.drawImage(image, 0, 0,
                    chunkWidth, chunkHeight,
                    chunkWidth * x, 0,
                    chunkWidth * x + chunkWidth,
                    chunkHeight, null); //����ͼƬ��xy���
            gr.dispose();

            images[x] = bufferedImage;
        }
    }

    public void step() {//�����˲����ķ���������10������¿�ʼ��4����
        frameSequence++;
        if(frameSequence >= DURATION) {
            frameSequence = 0;
            index++;
            if(index >= 4)
                index = 0;
        }
    }

    public Image getCurrentImage() {
        if(index<0 || index>=4)
            return null;
        return images[index];
    }
}
