package org.unimelb.whiteboard.client.whiteboard;

import org.unimelb.whiteboard.client.shape.MyShape;

import javax.swing.*;
import java.awt.*;

public class PaintBoardPanel extends JPanel {
    private final int x = 200;
    private final int y = 200;

    private final PaintManager paintManager;
    // Use to store the temporary shape.
    private MyShape bufferShape;
    private Image offScreenImage;

    public PaintBoardPanel(PaintManager paintManager) {
        super();
        this.paintManager = paintManager;
        bufferShape = null;
        this.setBackground(Color.WHITE);
    }

    public void clearBuffer() {
        bufferShape = null;
    }

    public void setBufferShape(MyShape bufferShape) {
        this.bufferShape = bufferShape;
    }

    /**
     * The function repaint can invoke this paint function.
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        // When you need to using the WindowBuilder to design the whiteBoardView, annotate all the code below.
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (MyShape myShape : paintManager.getPaintHistory()) {
            myShape.draw(g);
        }
        // When mouse haven't been released
        if (bufferShape != null) {
            bufferShape.draw(g);
        }
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(getWidth(), getHeight());
        }
        Graphics gImage = offScreenImage.getGraphics();
        paint(gImage);
        g.drawImage(offScreenImage, 0, 0, null);
    }
}
