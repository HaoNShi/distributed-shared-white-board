package org.unimelb.whiteboard.client.shape;

import java.awt.*;

public class MyCircle implements MyShape {
    private final MyPoint startP;
    private final int diameter;
    private final Color color;
    private final int thickness;


    public MyCircle(MyPoint startP, MyPoint endP, Color color, int thickness) {
        MyPoint temp;
        if (startP.getX() > endP.getX()) {
            if (startP.getY() > endP.getY()) {
                temp = endP;
            } else {
                temp = new MyPoint(endP.getX(), startP.getY());
            }
        } else {
            if (startP.getY() < endP.getY()) {
                temp = startP;
            } else {
                temp = new MyPoint(startP.getX(), endP.getY());
            }
        }
        this.diameter = (int) Math
                .sqrt(Math.pow(startP.getX() - endP.getX(), 2) + Math.pow(startP.getY() - endP.getY(), 2));
        int centerX = temp.getX() + Math.abs(startP.getX() - endP.getX()) / 2;
        int centerY = temp.getY() + Math.abs(startP.getY() - endP.getY()) / 2;
        this.startP = new MyPoint(centerX - this.diameter / 2, centerY - this.diameter / 2);
        this.color = color;
        this.thickness = thickness;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g.setColor(color);
        g.drawArc(startP.getX(), startP.getY(), diameter, diameter, 0, 360);
    }

}
