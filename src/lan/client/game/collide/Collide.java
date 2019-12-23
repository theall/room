package lan.client.game.collide;

import lan.client.game.sprite.Creature;
import lan.client.game.sprite.Frame;

import java.awt.*;

public class Collide {

    public static CollideDesc test(Creature creature1, Creature creature2) {
        CollideDesc collideDesc = new CollideDesc();
        Frame frame1 = creature1.getCurrentFrame();
        Frame frame2 = creature2.getCurrentFrame();
        Rectangle rect1 = frame1.getRectangle(creature1.getPos());
        Point leftTop1 = new Point(rect1.x, rect1.y);
        Point rightTop1 = new Point(rect1.x+rect1.width, rect1.y);
        Point leftBottom1 = new Point(rect1.x, rect1.y+rect1.height);
        Point rightBottom1 = new Point(rect1.x+rect1.width, rect1.y+rect1.height);

        Rectangle rect2 = frame2.getRectangle(creature2.getPos());
        Point leftTop2 = new Point(rect2.x, rect2.y);
        Point rightTop2 = new Point(rect2.x+rect2.width, rect2.y);
        Point leftBottom2 = new Point(rect2.x, rect2.y+rect2.height);
        Point rightBottom2 = new Point(rect2.x+rect2.width, rect2.y+rect2.height);

        Point speed1 = creature1.getSpeed();
        Point speed2 = creature2.getSpeed();
        int diffX = speed1.x - speed2.x;
        int diffY = speed1.y - speed2.y;

        Edge leftEdge1 = new Edge(Edge.Type.LEFT, leftTop1, leftBottom1);
        Edge topEdge1 = new Edge(Edge.Type.TOP, leftTop1, rightTop1);
        Edge rightEdge1 = new Edge(Edge.Type.RIGHT, rightTop1, rightBottom1);
        Edge bottomEdge1 = new Edge(Edge.Type.BOTTOM, leftBottom1, rightBottom1);

        Edge leftEdge2 = new Edge(Edge.Type.LEFT, leftTop2, leftBottom2);
        Edge topEdge2 = new Edge(Edge.Type.TOP, leftTop2, rightTop2);
        Edge rightEdge2 = new Edge(Edge.Type.RIGHT, rightTop2, rightBottom2);
        Edge bottomEdge2 = new Edge(Edge.Type.BOTTOM, leftBottom2, rightBottom2);

        Rectangle intersect = rect1.intersection(rect2);
        if(!intersect.isEmpty()) {
            if(diffX > 0) {
                collideDesc.edge = rightEdge1;
            } else if(diffX < 0){
                collideDesc.edge = leftEdge1;
            }
            if(diffY > 0) {
                collideDesc.edge = bottomEdge1;
            } else if(diffY <= 0){
                collideDesc.edge = topEdge1;
            }
            collideDesc.valid = true;
            collideDesc.centerX = (int)intersect.getCenterX();
            collideDesc.centerY = (int)intersect.getCenterY();
            collideDesc.correctX = intersect.width / 2;
            collideDesc.correctY = intersect.height / 2;
            return collideDesc;
        }
        if(diffX > 0) {
            int rightLeftDis = rightEdge1.distanceTo(leftEdge2);
            if(rightLeftDis<=0) {
                collideDesc.valid = true;
                collideDesc.edge = rightEdge1;
                collideDesc.correctX = rightLeftDis;
            }
        } else if(diffX < 0) {
            int leftRightDis = leftEdge1.distanceTo(rightEdge1);
            if(leftRightDis>=0 && leftRightDis<rect2.width) {
                collideDesc.valid = true;
                collideDesc.edge = leftEdge1;
                collideDesc.correctX = leftRightDis;
            }
        }

        if(diffY > 0) {
            int bottomTopDis = bottomEdge1.distanceTo(topEdge2);
            if(bottomTopDis<=0) {
                collideDesc.valid = true;
                collideDesc.edge = bottomEdge1;
                collideDesc.correctY = bottomTopDis;
            }
        } else if(diffY < 0) {
            int topBottomDis = topEdge1.distanceTo(bottomEdge2);
            if(topBottomDis>=0 && topBottomDis<rect2.height) {
                collideDesc.valid = true;
                collideDesc.edge = topEdge1;
                collideDesc.correctY = topBottomDis;
            }
        }
        return collideDesc;
    }

    public static Edge.Type getEdgeType(Rectangle master, Rectangle target) {
        Edge.Type edge = Edge.Type.BOTTOM;
        Point centerMaster = new Point((int)master.getCenterX(), (int)master.getCenterY());
        Point centerTarget = new Point((int)target.getCenterX(), (int)target.getCenterY());
        int disX = centerMaster.x - centerTarget.x;
        int disY = centerMaster.y - centerTarget.y;
        if(Math.abs(disX) < Math.abs(disY)) {
            // Top or bottom
            if(disY > 0) {
                // Bottom
                edge = Edge.Type.BOTTOM;
            } else {
                edge = Edge.Type.TOP;
            }
        } else {
            if(disX > 0) {
                // Bottom
                edge = Edge.Type.RIGHT;
            } else {
                edge = Edge.Type.LEFT;
            }
        }
        return edge;
    }
}
