package lan.client.game.collide;

import java.awt.*;

public class Edge {
    public enum Type {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

    public enum Dir {
        VERTICAL,
        HORIZONTAL
    }

    private Point start;
    private Point end;
    private Type type;
    private Dir dir;

    public Edge(Type type, Point start, Point end) {
        this.start = start;
        this.end = end;
        this.type = type;

        if (start.x == end.x) {
            dir = Dir.VERTICAL;
        } else if(start.y == end.y) {
            dir = Dir.HORIZONTAL;
        } else {
            System.out.println("Invalid parameters of line" + start.toString() + " and " + end.toString());
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public int distanceTo(Edge edge) {
        if(dir == Dir.VERTICAL) {
            if(edge.getDir() == Dir.VERTICAL) {
                if(pointBetween(start.y, edge.getStart().y, edge.getEnd().y) || pointBetween(end.y, edge.getStart().y, edge.getEnd().y)) {
                    return edge.getStart().x - start.x;
                }
            } else {

            }
        } else {
            // Horizontal
            if(edge.getDir() == Dir.VERTICAL) {

            } else {
                if(pointBetween(start.x, edge.getStart().x, edge.getEnd().x) || pointBetween(end.x, edge.getStart().x, edge.getEnd().x)) {
                    return edge.getStart().y - start.y;
                }
            }
        }
        return 0x7fffffff;
    }

    private boolean pointBetween(int point, int from, int to) {
        return point>=from && point<=to;
    }
}
