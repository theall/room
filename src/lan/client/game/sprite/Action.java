package lan.client.game.sprite;

import lan.client.game.base.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Action extends GameObject {
    private Frame[] frames = new Frame[4];
    private int index;
    private static int DURATION = 10;//持续时间
    private int frameSequence;

    public Action() {//活动
        frameSequence = 0;
        index = 0;
    }

    public void load(BufferedImage image) {
        int chunkWidth = image.getWidth() / 4;
        int chunkHeight = image.getHeight();
        for (int x = 0; x < 4; x++) {
            //设置小图的大小和类型
            BufferedImage bufferedImage = new BufferedImage(chunkWidth, chunkHeight, image.getType());

            //写入图像内容
            Graphics2D gr = bufferedImage.createGraphics();
            gr.drawImage(image, 0, 0,
                    chunkWidth, chunkHeight,
                    chunkWidth * x, 0,
                    chunkWidth * x + chunkWidth,
                    chunkHeight, null); //绘制图片的xy宽高
            gr.dispose();

            frames[x] = new Frame(bufferedImage);
        }
    }

    public void step() {//定义了步进的方法，超过10秒就重新开始到4结束
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
