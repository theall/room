package lan.client.game.sprite;

import lan.client.game.base.Dir;
import lan.client.game.base.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FourDirAnimation extends GameObject {
    private OrderedFrame[] animations = new OrderedFrame[4];//4行图
    private OrderedFrame currentAnimation;

    public void setCurrentAnimation(Dir dir) {
        switch (dir) {
            case UP:
                currentAnimation = animations[3];//当前图片
                break;
            case DOWN:
                currentAnimation = animations[0];
                break;
            case LEFT:
                currentAnimation = animations[1];
                break;
            case RIGHT:
                currentAnimation = animations[2];
                break;
        }
        currentAnimation.reset();
    }

    public Image getCurrentImage() {
        return currentAnimation.getCurrentImage();
    }

    public Frame getCurrentFrame() {
        return currentAnimation.getCurrentFrame();
    }

    public void load(BufferedImage image) {//切图
        int chunkWidth = image.getWidth();
        int chunkHeight = image.getHeight() / 4;
        int count = 0;
        for (int x = 0; x < 4; x++) {
            //设置小图的大小和类型
            BufferedImage bufferedImage = new BufferedImage(chunkWidth, chunkHeight, image.getType());

            //写入图像内容
            Graphics2D gr = bufferedImage.createGraphics();
            gr.drawImage(image, 0, 0,
                    chunkWidth, chunkHeight,
                    0, chunkHeight * x,
                    chunkWidth,
                    chunkHeight * (x + 1), null); //绘制图片的xy宽高
            gr.dispose();

            OrderedFrame animation = new OrderedFrame();
            animation.load(bufferedImage, 4);
            animations[x] = animation;
        }
        if (currentAnimation == null)
            currentAnimation = animations[0];
    }

    public void reset() {
        if(currentAnimation != null)
            currentAnimation.reset();
    }

    @Override
    public void step() {
        currentAnimation.step();
        if(currentAnimation.isEnd()) {
            currentAnimation.reset();
        }
    }

    @Override
    public void render(Graphics graphics) {

    }
}
