package lan.client.game.scene.layer;

import lan.client.game.base.RandomWrapper;
import lan.client.game.collide.Collide;

import lan.client.game.collide.Edge;
import lan.client.game.collide.QuadTree;
import lan.client.game.entity.Tank;
import lan.client.game.sprite.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * ÈËÎï£¬Ì¹¿Ë²ã
 */
public class ObjectLayer extends Layer {
    private ArrayList<Tank> tankList;
    private ArrayList<Rectangle> testRectList;
    private QuadTree quadTree;
    private Map<Rectangle, Sprite> spriteMap;

    public ObjectLayer() {
        tankList = new ArrayList<>();
        testRectList = new ArrayList<>();
        quadTree = new QuadTree(0, new Rectangle(0, 0, 1280, 1024));
        spriteMap = new HashMap<>();

        for(int i=0;i<20;i++) {
            for(int j=0;j<20;j++) {
                Rectangle testRect = new Rectangle(i*100+10, j*100+10, 24, 24);
                testRectList.add(testRect);
            }
        }
    }

    public void load(ArrayList<BufferedImage> imageArrayList) {
        RandomWrapper randomWrapper = RandomWrapper.getInstance();
        for(BufferedImage image : imageArrayList) {
            Tank tank = new Tank();
            tank.load(image);
            tank.setPos(randomWrapper.nextInt(100, 200), randomWrapper.nextInt(100, 200));
            tankList.add(tank);
        }
    }

    public Tank getRandomTank() {
        RandomWrapper randomWrapper = RandomWrapper.getInstance();
        return tankList.get(randomWrapper.nextInt(tankList.size()));
    }

    @Override
    public void step() {
        quadTree.clear();
        spriteMap.clear();
        for(Tank tank : tankList) {
            tank.step();
            Rectangle tankRect = tank.getRectangle();
            spriteMap.put(tankRect, tank);
            quadTree.insert(tankRect);
        }
        for(Rectangle testRectangle : testRectList) {
            spriteMap.put(testRectangle, null);
            quadTree.insert(testRectangle);
        }
        for(Map.Entry<Rectangle, Sprite> entry : spriteMap.entrySet()) {
            Rectangle tankRect = entry.getKey();
            Sprite tank = entry.getValue();
            if(tank == null)
                continue;

            ArrayList<Rectangle> retrieveRectList = new ArrayList<>();
            quadTree.retrieve(retrieveRectList, tankRect);
            for(Rectangle rectangle : retrieveRectList) {
                Rectangle intersect = tankRect.intersection(rectangle);

                if(!intersect.isEmpty()) {
                    Point pos = tank.getPos();
                    intersect.grow(1,1);
                    Edge.Type edgetTpye = Collide.getEdgeType(tankRect, rectangle);
                    if(edgetTpye == Edge.Type.RIGHT) {
                        if(intersect.contains(tankRect.x, tankRect.y) || intersect.contains(tankRect.x, tankRect.y+tankRect.height)) {
                            tank.setPos(pos.x+intersect.width-2, pos.y);
                        }
                    } else if(edgetTpye == Edge.Type.LEFT) {
                        if(intersect.contains(tankRect.x+tankRect.width, tankRect.y) || intersect.contains(tankRect.x+tankRect.width, tankRect.y+tankRect.height)) {
                            tank.setPos(pos.x-intersect.width+2, pos.y);
                        }
                    } else if(edgetTpye == Edge.Type.TOP) {
                        if(intersect.contains(tankRect.x, tankRect.y+tankRect.height) || intersect.contains(tankRect.x+tankRect.width, tankRect.y+tankRect.height)) {
                            tank.setPos(pos.x, pos.y-intersect.height+2);
                        }
                    } else if(edgetTpye == Edge.Type.BOTTOM) {
                        if(intersect.contains(tankRect.x, tankRect.y) || intersect.contains(tankRect.x+tankRect.width, tankRect.y)) {
                            tank.setPos(pos.x, pos.y+intersect.height-2);
                        }
                    }
                }
            }
        }
    }

    public Sprite getSprite(int x, int y) {
        for(Tank tank : tankList) {
            if(tank.getRectangle().contains(x,y))
                return tank;
        }
        return null;
    }

    @Override
    public void render(Graphics g) {
        for(Tank tank : tankList) {
            tank.render(g);
        }

        for(Rectangle testRect : testRectList) {
            g.drawRect(testRect.x, testRect.y, testRect.width, testRect.height);
        }
    }
}
