package org.unimelb.whiteboard.client.shape;

import java.awt.*;

/**
 * My Line
 */
public class MyLine implements MyShape {
    private final MyPoint startP;
    private final MyPoint endP;
    Color color;
    int thickness;

    public MyLine(MyPoint startP, MyPoint endP, Color color, int thickness) {
        this.startP = startP;
        this.endP = endP;
        this.color = color;
        this.thickness = thickness;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g.setColor(color);
        g.drawLine(startP.getX(), startP.getY(), endP.getX(), endP.getY());
    }
}
