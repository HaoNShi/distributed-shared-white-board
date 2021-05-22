package org.unimelb.whiteboard.client.shape;

import java.awt.*;

public class MyText implements MyShape {
    private final MyPoint startP;
    private final String text;
    private final int size;
    private final Color color;

    public MyText(MyPoint startP, String text, int size, Color color) {
        this.startP = startP;
        this.text = text;
        this.size = size;
        this.color = color;
    }

    public void draw(Graphics2D g) {
        Font font = new Font(Font.DIALOG, Font.PLAIN, this.size);
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, startP.getX(), startP.getY());
    }
}
