package org.unimelb.whiteboard.client.WhiteBoard;

import org.unimelb.whiteboard.client.Shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawListener extends MouseAdapter implements ActionListener {
    private final WhiteBoardWindow wbv;
    private MyPoint startP, endP;
    private String toolName = "pen";
    private Color color = null;
    private Image drawBuffer = null;
    private int thickness = 2;
    private MyFreeDraw currentFreeDraw = null;

    DrawListener(WhiteBoardWindow wbv) {
        currentFreeDraw = null;
        this.wbv = wbv;
        color = wbv.getCurrentColor();
        this.thickness = wbv.getThickness();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("")) {
            JButton button = (JButton) e.getSource();
            color = wbv.getCurrentColor();
            thickness = wbv.getThickness();
        } else {
            JButton button = (JButton) e.getSource();
            toolName = button.getActionCommand();
        }
    }

    public void mousePressed(MouseEvent e) {
//		System.out.println("Operation: " + "mousePressed " + toolName);
        color = wbv.getCurrentColor();
        thickness = wbv.getThickness();
        startP = new MyPoint(e.getX(), e.getY());
        drawBuffer = wbv.getPaintBoardPanel().createImage(wbv.getPaintBoardPanel().getWidth(),
                wbv.getPaintBoardPanel().getHeight());
    }

    public void mouseReleased(MouseEvent e) {
//		System.out.println("Operation: " + "mouseReleased " + toolName);
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
                currentFreeDraw = new MyFreeDraw(MyFreeDraw.PEN, startP, endP, color, thickness);
            } else {
                currentFreeDraw.addPoints(endP);
            }
            myShape = currentFreeDraw;
            currentFreeDraw = null;
        } else if (toolName.equals("eraser")) {
            if (currentFreeDraw == null) {
                currentFreeDraw = new MyFreeDraw(MyFreeDraw.ERASER, startP, endP, wbv.getBackgroundColor(), thickness);
            } else {
                currentFreeDraw.addPoints(endP);
            }
            myShape = currentFreeDraw;
            currentFreeDraw = null;
        } else if (toolName.equals("text")) {
            String text = JOptionPane.showInputDialog(wbv.getFrame(), "Text:");
            if (text != null && text != "") {
                myShape = new MyText(endP, text, thickness * 10, color);
            }
        } else {
            System.out.println("Error: Unknown Tool Name!");
        }
//		wbv.getPaintBoardPanel().revalidate();
        wbv.getPaintBoardPanel().setBufferShape(null);
        if (myShape != null) wbv.getPaintManager().addShape(myShape);
    }

    public void mouseDragged(MouseEvent e) {
        endP = new MyPoint(e.getX(), e.getY());
        MyShape bufferShape = null;
        if (toolName.equals("pen")) {
            if (currentFreeDraw == null) {
                currentFreeDraw = new MyFreeDraw(MyFreeDraw.PEN, startP, endP, color, thickness);
            } else {
                currentFreeDraw.addPoints(endP);
            }
            bufferShape = currentFreeDraw;
        } else if (toolName.equals("eraser")) {
            if (currentFreeDraw == null) {
                currentFreeDraw = new MyFreeDraw(MyFreeDraw.ERASER, startP, endP, wbv.getBackgroundColor(), thickness);
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
            System.out.println("Error: Unkown Tool Name!");
        }
        // When working on RMI, no need to upload bufferShape. Only show in client window.
        wbv.getPaintBoardPanel().setBufferShape(bufferShape);
        wbv.getPaintBoardPanel().repaint();
    }

}
