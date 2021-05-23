package org.unimelb.whiteboard.client.whiteboard;

import org.unimelb.whiteboard.client.shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawListener extends MouseAdapter implements ActionListener {
    private final WhiteBoardWindow window;
    private MyPoint startP, endP;
    private String toolName = "pen";
    private Color color;
    private Image drawBuffer = null;
    private int thickness = 2;
    private MyPen currentFreeDraw;

    DrawListener(WhiteBoardWindow window) {
        currentFreeDraw = null;
        this.window = window;
        color = window.getCurrentColor();
        this.thickness = window.getThickness();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("")) {
            JButton button = (JButton) e.getSource();
            color = window.getCurrentColor();
            thickness = window.getThickness();
        } else {
            JButton button = (JButton) e.getSource();
            toolName = button.getActionCommand();
        }
    }

    public void mousePressed(MouseEvent e) {
        color = window.getCurrentColor();
        thickness = window.getThickness();
        startP = new MyPoint(e.getX(), e.getY());
        drawBuffer = window.getPaintBoardPanel().createImage(window.getPaintBoardPanel().getWidth(),
                window.getPaintBoardPanel().getHeight());
    }

    public void mouseReleased(MouseEvent e) {
        endP = new MyPoint(e.getX(), e.getY());
        MyShape myShape = null;
        if (toolName.equals("line")) {
            myShape = new MyLine(startP, endP, color, thickness);
        } else if (toolName.equals("circle")) {
            myShape = new MyCircle(startP, endP, color, thickness);
        } else if (toolName.equals("rect")) {
            myShape = new MyRect(false, startP, endP, color, thickness);
        } else if (toolName.equals("roundrect")) {
            myShape = new MyRect(true, startP, endP, color, thickness);
        } else if (toolName.equals("oval")) {
            myShape = new MyOval(startP, endP, color, thickness);
        } else if (toolName.equals("pen")) {
            if (currentFreeDraw == null) {
                currentFreeDraw = new MyPen(MyPen.PEN, startP, endP, color, thickness);
            } else {
                currentFreeDraw.addPoints(endP);
            }
            myShape = currentFreeDraw;
            currentFreeDraw = null;
        } else if (toolName.equals("eraser")) {
            if (currentFreeDraw == null) {
                currentFreeDraw = new MyPen(MyPen.ERASER, startP, endP, window.getBackgroundColor(), thickness);
            } else {
                currentFreeDraw.addPoints(endP);
            }
            myShape = currentFreeDraw;
            currentFreeDraw = null;
        } else if (toolName.equals("text")) {
        	ImageIcon TextIcon = new ImageIcon("images/Text1.png");
            String text = (String) JOptionPane.showInputDialog(window.getFrame(), "Input your content:", "Text", JOptionPane.INFORMATION_MESSAGE, TextIcon, null, null);
            if (text != null && !text.equals("")) {
                myShape = new MyText(endP, text, thickness * 10, color);
            }
        } else {
            System.out.println("Error: Unknown Tool Name!");
        }
        window.getPaintBoardPanel().setBufferShape(null);
        if (myShape != null) window.getPaintManager().addShape(myShape);
    }

    public void mouseDragged(MouseEvent e) {
        endP = new MyPoint(e.getX(), e.getY());
        MyShape bufferShape = null;
        if (toolName.equals("pen")) {
            if (currentFreeDraw == null) {
                currentFreeDraw = new MyPen(MyPen.PEN, startP, endP, color, thickness);
            } else {
                currentFreeDraw.addPoints(endP);
            }
            bufferShape = currentFreeDraw;
        } else if (toolName.equals("eraser")) {
            if (currentFreeDraw == null) {
                currentFreeDraw = new MyPen(MyPen.ERASER, startP, endP, window.getBackgroundColor(), thickness);
            } else {
                currentFreeDraw.addPoints(endP);
            }
            bufferShape = currentFreeDraw;
        } else if (toolName.equals("line")) {
            bufferShape = new MyLine(startP, endP, color, thickness);
        } else if (toolName.equals("circle")) {
            bufferShape = new MyCircle(startP, endP, color, thickness);
        } else if (toolName.equals("rect")) {
            bufferShape = new MyRect(false, startP, endP, color, thickness);
        } else if (toolName.equals("roundrect")) {
            bufferShape = new MyRect(true, startP, endP, color, thickness);
        } else if (toolName.equals("oval")) {
            bufferShape = new MyOval(startP, endP, color, thickness);
        } else if (toolName.equals("text")) {
            bufferShape = null;
        } else {
            System.out.println("Error: Unknown Tool Name!");
        }
        // When working on RMI, no need to upload bufferShape. Only show in client window.
        window.getPaintBoardPanel().setBufferShape(bufferShape);
        window.getPaintBoardPanel().repaint();
    }

}
