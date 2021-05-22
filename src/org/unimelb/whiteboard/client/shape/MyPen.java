package org.unimelb.whiteboard.client.shape;

import java.awt.*;
import java.util.Vector;

/**
 * Use for pen and eraser.
 */
public class MyPen implements MyShape {
    public static final int PEN = 0;
    public static final int SPRAY = 1;
    public static final int ERASER = 1;

    private final Vector<MyPoint> points;
    private final Color color;
    private final int thickness;
    private final int mode;
    private int isSpray;

    public MyPen(int mode, MyPoint firstP, MyPoint nextP, Color color, int thickness) {
        this.mode = mode;
        this.points = new Vector<>();
        this.points.add(firstP);
        this.points.add(nextP);
        this.color = color;
        this.thickness = thickness;
    }

    public void addPoints(MyPoint nextP) {
        this.points.add(nextP);
    }

    public void draw(Graphics2D g) {
        BasicStroke bs = null;
        if (mode == PEN) {
            g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        } else if (mode == ERASER) {
            g.setStroke(new BasicStroke(thickness * 2 + 40, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        }
        g.setColor(color);
        for (int i = 0; i < points.size() - 1; i++) {
            g.drawLine(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
        }
    }
}
