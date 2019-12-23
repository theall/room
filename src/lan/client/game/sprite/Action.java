package lan.client.game.sprite;

import lan.client.game.base.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Action extends GameObject {
    private Frame[] frames = new Frame[4];
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

            frames[x] = new Frame(bufferedImage);
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

    @Override
    public void render(Graphics g) {

    }

    public Frame getCurrentFrame() {
        if(index<0 || index>=4)
            return null;

        return frames[index];
    }

    public Image getCurrentImage() {
        Frame frame = getCurrentFrame();
        if(frame == null)
            return null;
        return frame.getImage();
    }
}
