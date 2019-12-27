package lan.client.game.scene.layer;

import lan.client.game.map.MapParser;

import java.awt.*;

/**
 * µØÐÎ²ã
 */
public class TerrianLayer extends Layer {
    private Image[][] images;
    private int tileHeight = 32;
    private int tileWidth = 32;

    @Override
    public void step() {

    }

    public void load() {
        MapParser mapParser = new MapParser("resources/map/map1.json");
        images = mapParser.parse();
        System.out.print(images.length);
    }

    @Override
    public void render(Graphics g) {
        for(int i=0;i<images.length;i++) {
            int y = i * tileHeight;
            for(int j=0;j<images[i].length;j++) {
                int x = j * tileWidth;
                g.drawImage(images[i][j], x, y, null);
            }
        }
    }

    public static void main(String[] args) {
        TerrianLayer terrianLayer = new TerrianLayer();
        terrianLayer.load();
    }
}
