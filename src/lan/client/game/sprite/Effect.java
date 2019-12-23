package lan.client.game.sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Effect extends Sprite {
    private ArrayList<BufferedImage> imageArrayList;
    private int currentIndex;

    public Effect() {
        currentIndex = 0;
    }

    public void load(BufferedImage image) {

    }

    public boolean isEnd() {
        return currentIndex>=imageArrayList.size();
    }

    @Override
    public void step() {
        currentIndex++;
    }

    @Override
    public void render(Graphics graphics) {//‰÷»æ
        super.step();
    }
}
