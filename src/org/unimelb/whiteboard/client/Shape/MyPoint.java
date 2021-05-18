package org.unimelb.whiteboard.client.Shape;

import java.awt.*;
import java.io.Serializable;

/**
 * My Point
 */
public class MyPoint implements Serializable {
    private int x;
    private int y;

    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static final void switchPoints(MyPoint p1, MyPoint p2) {
        MyPoint temp;
        temp = p1;
        p1 = p2;
        p2 = p1;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Point p) {
        // TODO Auto-generated method stub
        return (this.x == p.getX() && this.y == p.getY());
    }
}
