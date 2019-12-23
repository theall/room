package lan.client.game.sprite;

import lan.client.game.base.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Frame is wrapper of image.
 */
public class Frame extends GameObject {
    private BufferedImage image;
    private Rectangle rectangle;
    private int duration;
    private int frameSequence;
    private static int DEFAULT_DURATION = 10;//帧默认持续时间

    public Frame(BufferedImage image) {
        frameSequence = 0;
        duration = DEFAULT_DURATION;
        this.image = image;

        if(image != null)
            rectangle = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        else
            rectangle = new Rectangle();
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Rectangle getRectangle(Point pos) {
        Rectangle temp = new Rectangle(rectangle);
        temp.x = pos.x - image.getWidth()/2;
        temp.y = pos.y - image.getHeight()/2;
        return temp;
    }

    public boolean isEnd() {
        return frameSequence > duration;
    }

    public void reset() {
        frameSequence = 0;
    }

    @Override
    public void step() {
        frameSequence++;
    }

    @Override
    public void render(Graphics g) {

    }
}
