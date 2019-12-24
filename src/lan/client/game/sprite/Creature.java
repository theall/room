package lan.client.game.sprite;

import lan.client.game.base.Button;
import lan.client.game.base.Dir;
import lan.client.game.base.RandomWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Creature extends Sprite {
    private Random random;
    private FourDirAnimation fourDirAnimation;
    private Dir dir;
    private float speed = 2f;
    private Button button;

    public Creature() {
        random = new Random(System.currentTimeMillis());
        fourDirAnimation = new FourDirAnimation();
    }

    public void bindButton(Button button) {
        this.button = button;
    }

    public Button getBindButton() {
        return button;
    }

    public void generateRandomDir() {
        int i = RandomWrapper.getInstance().nextInt(Dir.values().length);
        Dir dir = Dir.values()[i];
        setDir(dir);
    }

    public void load(BufferedImage image) {
        fourDirAnimation.load(image);
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
        fourDirAnimation.setCurrentAnimation(dir);
        updateSpeed();
    }

    public void setForwardSpeed(float speed) {
        this.speed = speed;
        updateSpeed();
    }

    private void updateSpeed() {
        switch (dir) {
            case UP:
                setSpeed(0, -speed);//×ø±ê
                break;
            case DOWN:
                setSpeed(0, speed);
                break;
            case LEFT:
                setSpeed(-speed, 0);
                break;
            case RIGHT:
                setSpeed(speed, 0);
                break;
        }
    }

    public Frame getCurrentFrame() {
        return fourDirAnimation.getCurrentFrame();
    }

    public Rectangle getRectangle() {
        return getCurrentFrame().getRectangle(getPos());
    }

    @Override
    public void step() {//²½½ø
        super.step();

        if(button != null) {
            if(button.test(Button.Type.UP)) {
                setDir(Dir.UP);
            } else if(button.test(Button.Type.DOWN)) {
                setDir(Dir.DOWN);
            } else if(button.test(Button.Type.LEFT)) {
                setDir(Dir.LEFT);
            } else if(button.test(Button.Type.RIGHT)) {
                setDir(Dir.RIGHT);
            } else {
                setSpeed(0, 0);
            }
        }

        fourDirAnimation.step();
        Image image = fourDirAnimation.getCurrentImage();
        setImage(image);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        Point point = getPos();

        Rectangle rectangle = fourDirAnimation.getCurrentFrame().getRectangle(getPos());
        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}