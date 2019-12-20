package lan.client.game.sprite;

import lan.client.game.DemoFrame;

import java.awt.*;

public class Sprite { //画面
    private Image image;//图片
    private float x;//速度
    private float y;
    private float sx;//坐标速度
    private float sy;
    public Sprite() {
        x = 0;
        y = 0;
    }

    public void setSpeed(float sx, float sy) {
        this.sx = sx;
        this.sy = sy;
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void step() {
        this.x += sx;
        this.y += sy;
    }

    public void render(Graphics graphics) {//渲染
        if(image != null)
            graphics.drawImage(image, (int)x, (int)y, null);
    }


    public void setImage(Image image) {
        this.image = image;
    }
}
