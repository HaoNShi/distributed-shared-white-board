package org.unimelb.whiteboard.client.Shape;

import java.awt.*;

/**
 * My Oval Object
 */
public class MyOval implements MyShape {
    private final MyPoint startP;
    private final int width;
    private final int height;
    private final Color color;
    private final int thickness;

    public MyOval(MyPoint startP, MyPoint endP, Color color, int thickness) {
        this.color = color;
        this.thickness = thickness;
        this.width = Math.abs(startP.getX() - endP.getX());
        this.height = Math.abs(startP.getY() - endP.getY());
        if (startP.getX() > endP.getX()) {
            if (startP.getY() < endP.getY()) {
                this.startP = new MyPoint(endP.getX(), startP.getY());
            } else {
                this.startP = endP;
            }
        } else {
            if (startP.getY() > endP.getY()) {
                this.startP = new MyPoint(startP.getX(), endP.getY());
            } else {
                this.startP = startP;
            }
        }
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g.setColor(color);
        g.drawOval(startP.getX(), startP.getY(), width, height);
    }
}
