package lan.client.game.sprite;

import lan.client.game.base.GameObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Sprite extends GameObject { //画面
    private Image image;//图片
    private float _x;
    private float _y;
    private float x;//速度
    private float y;
    private float sx;//坐标速度
    private float sy;
    private int stepCount;
    private float rotate;

    public Sprite() {
        x = 0;
        y = 0;
        stepCount = 0;
        rotate = 0f;
    }

    public void setSpeed(float sx, float sy) {
        this.sx = sx;
        this.sy = sy;
    }

    public void setPos(float x, float y) {
        _x = x;
        _y = y;
        this.x = x;
        this.y = y;
    }

    public Point getPos() {
        return new Point((int)x, (int)y);
    }

    public Point getSpeed() {
        return new Point((int)sx, (int)sy);
    }

    public Rectangle getStepRect() {
        int minX = Math.round(Math.min(_x, x));
        int minY = Math.round(Math.min(_y, y));
        int maxX = Math.round(Math.max(_x, x));
        int maxY = Math.round(Math.max(_y, y));
        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(minX, minY, maxX-minX, maxY-minY);
        return rectangle;
    }

    public int getStepCount() {
        return stepCount;
    }

    @Override
    public void step() {
        _x = x;
        _y = y;
        this.x += sx;
        this.y += sy;
        stepCount++;
    }

    @Override
    public void render(Graphics graphics) {//渲染
        if(image != null) {
            graphics.drawImage(image, (int) Math.ceil(x - image.getWidth(null) / 2 + 0.5), (int) Math.ceil(y - image.getHeight(null) / 2 + 0.5), null);
        }
        graphics.fillRect((int)x-1, (int)y-1, 3, 3);
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
