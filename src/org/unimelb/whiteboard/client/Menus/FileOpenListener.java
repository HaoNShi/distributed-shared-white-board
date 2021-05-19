package org.unimelb.whiteboard.client.Menus;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.Shape.MyShape;
import org.unimelb.whiteboard.client.WhiteBoard.WhiteBoardWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;

public class FileOpenListener implements ActionListener {
    private final JFileChooser chooser;
    private final WhiteBoardWindow wbv;
    private final Client client;
    private String currentPath;

    public FileOpenListener(WhiteBoardWindow wbv, Client client) {
        super();
        this.wbv = wbv;
        this.client = client;
        chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("WhiteBoard files", "wb");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setAccessory(new ImagePreviewer(chooser));
        chooser.setFileView(new FileImageView(filter, new ImageIcon()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Operation: Open File.");
        chooser.setCurrentDirectory(new File("."));
        int returnVal = chooser.showOpenDialog(wbv.getFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                currentPath = chooser.getSelectedFile().getPath();
                client.setCurrentSavePath(currentPath);
                System.out.println(currentPath);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(currentPath));
                Vector<MyShape> history = (Vector<MyShape>) ois.readObject();
                wbv.getPaintManager().clearRedoHistory();
                wbv.getPaintManager().setPaintHistory(history);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    protected String getImagePath() {
        return currentPath;
    }
}
