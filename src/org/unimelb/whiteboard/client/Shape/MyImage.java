package org.unimelb.whiteboard.client.Shape;

import java.awt.*;

/**
 * My Image
 */
public class MyImage implements MyShape {
    private final MyPoint startP;
    private Image image = null;

    public MyImage(MyPoint startP, Image image) {
        this.startP = startP;
        this.image = image;
    }

    public void draw(Graphics2D g) {
        try {
            if (image != null) g.drawImage(image, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
