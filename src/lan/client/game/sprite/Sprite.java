package lan.client.game.sprite;

import lan.client.game.DemoFrame;

import java.awt.*;

public class Sprite { //����
    private Image image;//ͼƬ
    private float x;//�ٶ�
    private float y;
    private float sx;//�����ٶ�
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

    public void render(Graphics graphics) {//��Ⱦ
        if(image != null)
            graphics.drawImage(image, (int)x, (int)y, null);
    }


    public void setImage(Image image) {
        this.image = image;
    }
}
