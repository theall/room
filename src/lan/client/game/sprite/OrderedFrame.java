package lan.client.game.sprite;

import lan.client.game.base.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class OrderedFrame extends GameObject {
    private ArrayList<Frame> frames;
    private int index;
    private Frame currentFrame;

    public OrderedFrame() {//�
        frames = new ArrayList<>();
        init();
    }

    public void load(BufferedImage image, int size) {
        int chunkWidth = image.getWidth() / size;
        int chunkHeight = image.getHeight();
        for (int x = 0; x < size; x++) {
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

            Frame frame = new Frame(bufferedImage);
            frames.add(frame);
        }
        init();
    }

    public void step() {//�����˲����ķ���������10������¿�ʼ��4����
        if(currentFrame == null)
            return;

        currentFrame.step();
        if(currentFrame.isEnd()) {
            index++;
            currentFrame.reset();
            currentFrame = getCurrentFrame();
        }
    }

    public void addFrames(ArrayList<Frame> frameList) {
        frames.clear();
        frames.addAll(frameList);
        init();
    }

    public void addFrame(Frame frame) {
        frames.add(frame);
    }

    public void setCurrentIndex(int index) {
        this.index = index;
        currentFrame = getCurrentFrame();
    }

    @Override
    public void render(Graphics g) {

    }

    public Frame getCurrentFrame() {
        if(index<0 || index>=frames.size())
            return null;

        return frames.get(index);
    }

    public Image getCurrentImage() {
        Frame frame = getCurrentFrame();
        if(frame == null)
            return null;
        return frame.getImage();
    }

    private void init() {
        index = 0;
        currentFrame = getCurrentFrame();
    }

    public boolean isEnd() {
        return index >= frames.size();
    }

    public void reset() {
        index = 0;
    }
}
