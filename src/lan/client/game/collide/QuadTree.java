package lan.client.game.collide;

import java.awt.*;
import java.util.ArrayList;

public class QuadTree {
    private static int MAX_OBJECTS = 10;
    private static int MAX_LEVELS = 5;
    private int level;
    private ArrayList<Rectangle> objects;
    private Rectangle bounds;
    private QuadTree[] nodes;

    /**
     * Constructor
     */
    public QuadTree(int pLevel, Rectangle pBounds) {
        level = pLevel;
        objects = new ArrayList<>();
        bounds = pBounds;
        nodes = new QuadTree[4];
    }

    /**
     * Clear this node
     */
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    /**
     * Split this node into four sub nodes
     */
    private void split() {
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        int subWidth = (int) bounds.getWidth() / 2;
        int subHeight = (int) bounds.getHeight() / 2;

        int nextLevel = level + 1;
        nodes[0] = new QuadTree(nextLevel, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new QuadTree(nextLevel, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new QuadTree(nextLevel, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new QuadTree(nextLevel, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    /**
     * Determine which node the object belongs to
     */
    public int getIndex(Rectangle pRect) {
        int index = -1;
        double horizontalMidpoint = bounds.getCenterX();
        double verticalMidpoint = bounds.getCenterY();
        // Object can completely fit within the top quadrants
        boolean topQuadrant = pRect.getY() + pRect.getHeight() < verticalMidpoint;
        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = pRect.getY() > verticalMidpoint;
        // Object can completely fit within the left quadrants
        if (pRect.getX() + pRect.getWidth() < horizontalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } else if (pRect.getX() > horizontalMidpoint) {// Object can completely fit within the right quadrants
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }
        return index;
    }

    /**
     * Insert the object into the quadtree. If the node
     * exceeds the capacity, it will split and add all
     * objects to their corresponding nodes.
     */
    public void insert(Rectangle pRect) {
        if (nodes[0] != null) {
            int index = getIndex(pRect);
            if(index != -1) {
                nodes[index].insert(pRect);
                return;
            }
        }
        objects.add(pRect);
        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if(nodes[0] == null) {
                split();
            }
            int i = 0;
            while(i < objects.size()) {
                int index = getIndex(objects.get(i));
                if(index != -1) {
                    nodes[index].insert(objects.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    /*

     * Return all objects that could collide with the given object

     */

    public ArrayList<Rectangle> retrieve(ArrayList<Rectangle> returnObjects, Rectangle pRect) {
        int index = getIndex(pRect);
        if(index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, pRect);
        }
        returnObjects.addAll(objects);
        if(returnObjects.indexOf(pRect) != -1)
            returnObjects.remove(pRect);
        return returnObjects;
    }
}
