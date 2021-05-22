package org.unimelb.whiteboard.client.shape;

import java.awt.*;
import java.io.Serializable;

/**
 * My Shape interface
 */
public interface MyShape extends Serializable {
    void draw(Graphics2D g);
}
