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
        switch (toolName) {
            case "line":
                myShape = new MyLine(startP, endP, color, thickness);
                break;
            case "circle":
                myShape = new MyCircle(startP, endP, color, thickness);
                break;
            case "rect":
                myShape = new MyRect(false, startP, endP, color, thickness);
                break;
            case "roundrect":
                myShape = new MyRect(true, startP, endP, color, thickness);
                break;
            case "oval":
                myShape = new MyOval(startP, endP, color, thickness);
                break;
            case "pen":
                if (currentFreeDraw == null) {
                    currentFreeDraw = new MyPen(MyPen.PEN, startP, endP, color, thickness);
                } else {
                    currentFreeDraw.addPoints(endP);
                }
                myShape = currentFreeDraw;
                currentFreeDraw = null;
                break;
            case "eraser":
                if (currentFreeDraw == null) {
                    currentFreeDraw = new MyPen(MyPen.ERASER, startP, endP, window.getBackgroundColor(), thickness);
                } else {
                    currentFreeDraw.addPoints(endP);
                }
                myShape = currentFreeDraw;
                currentFreeDraw = null;
                break;
            case "text":
                ImageIcon TextIcon = new ImageIcon("images/text_dark.png");
                String text = (String) JOptionPane.showInputDialog(window.getFrame(), "Text:", "Input", JOptionPane.INFORMATION_MESSAGE, TextIcon, null, null);
                if (text != null && !text.equals("")) {
                    myShape = new MyText(endP, text, thickness * 10, color);
                }
                break;
            default:
                System.err.println("Error: Unknown Tool Name!");
                break;
        }
        window.getPaintBoardPanel().setBufferShape(null);
        if (myShape != null) window.getPaintManager().addShape(myShape);
    }

    public void mouseDragged(MouseEvent e) {
        endP = new MyPoint(e.getX(), e.getY());
        MyShape bufferShape = null;
        switch (toolName) {
            case "pen":
                if (currentFreeDraw == null) {
                    currentFreeDraw = new MyPen(MyPen.PEN, startP, endP, color, thickness);
                } else {
                    currentFreeDraw.addPoints(endP);
                }
                bufferShape = currentFreeDraw;
                break;
            case "eraser":
                if (currentFreeDraw == null) {
                    currentFreeDraw = new MyPen(MyPen.ERASER, startP, endP, window.getBackgroundColor(), thickness);
                } else {
                    currentFreeDraw.addPoints(endP);
                }
                bufferShape = currentFreeDraw;
                break;
            case "line":
                bufferShape = new MyLine(startP, endP, color, thickness);
                break;
            case "circle":
                bufferShape = new MyCircle(startP, endP, color, thickness);
                break;
            case "rect":
                bufferShape = new MyRect(false, startP, endP, color, thickness);
                break;
            case "roundrect":
                bufferShape = new MyRect(true, startP, endP, color, thickness);
                break;
            case "oval":
                bufferShape = new MyOval(startP, endP, color, thickness);
                break;
            case "text":
                bufferShape = null;
                break;
            default:
                System.out.println("Error: Unknown Tool Name!");
                break;
        }
        // When working on RMI, no need to upload bufferShape. Only show in client window.
        window.getPaintBoardPanel().setBufferShape(bufferShape);
        window.getPaintBoardPanel().repaint();
    }

}
