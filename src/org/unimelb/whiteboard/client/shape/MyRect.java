package org.unimelb.whiteboard.client.shape;

import java.awt.*;

/**
 * My Rectangle
 */
public class MyRect implements MyShape {
    private final int width;
    private final int height;
    private final Color color;
    private final int thickness;
    private final Boolean isRoundRect;
    private MyPoint startP;

    public MyRect(Boolean isRoundRect, MyPoint startP, MyPoint endP, Color color, int thickness) {
        this.startP = startP;
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
        this.color = color;
        this.thickness = thickness;
        this.isRoundRect = isRoundRect;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g.setColor(color);
        if (isRoundRect) {
            g.drawRoundRect(startP.getX(), startP.getY(), width, height, 2, 10);
        } else {
            g.drawRect(startP.getX(), startP.getY(), width, height);
        }
    }
}
