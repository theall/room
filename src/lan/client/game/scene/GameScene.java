package lan.client.game.scene;

import lan.client.game.base.GameObject;
import lan.client.game.scene.layer.EffectLayer;
import lan.client.game.scene.layer.ObjectLayer;
import lan.client.game.scene.layer.TerrianLayer;
import lan.client.game.scene.layer.UILayer;
import lan.client.game.sprite.Sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GameScene extends GameObject {
    private TerrianLayer terrianLayer;
    private ObjectLayer objectLayer;
    private EffectLayer effectLayer;
    private UILayer uiLayer;

    public GameScene() {
        terrianLayer = new TerrianLayer();
        objectLayer = new ObjectLayer();
        effectLayer = new EffectLayer();
        uiLayer = new UILayer();
    }

    public Sprite getSprite(int x, int y) {
        return objectLayer.getSprite(x, y);
    }

    public void load() {
        ArrayList<BufferedImage> imageArrayList = new ArrayList<>();
        imageArrayList.add(getBufferedImage("/resources/tank/no7_mm1/no7_mm3.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/no7_mm2/no7_mm2r.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/no8_4_mm1/no8_4_mm2.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/no8_mm1/no8_mm1.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/nukem/character.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/palu/character.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/rat_mm1/rat_mm1.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/rommel/character.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/tax/tax1.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/tax/tax2.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/tax/tax3.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/tax/tax4.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/tax/tax5.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/tax/tax6.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/tax/tax7.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/tiger_mm1/tiger_mm1.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/wen_mm1/wen_mm1.png"));
        imageArrayList.add(getBufferedImage("/resources/tank/wolf_mm1/wolf_mm1.png"));
        objectLayer.load(imageArrayList);
    }

    private BufferedImage getBufferedImage(String fileName) {
        URL imageURL = this.getClass().getResource(fileName);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(GameScene.class.getResource(fileName));//´«Í¼Æ¬
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public TerrianLayer getTerrianLayer() {
        return terrianLayer;
    }

    public ObjectLayer getObjectLayer() {
        return objectLayer;
    }

    public EffectLayer getEffectLayer() {
        return effectLayer;
    }

    public UILayer getUiLayer() {
        return uiLayer;
    }

    @Override
    public void step() {
        objectLayer.step();
        effectLayer.step();
    }

    @Override
    public void render(Graphics g) {
        objectLayer.render(g);
        effectLayer.render(g);
    }
}
