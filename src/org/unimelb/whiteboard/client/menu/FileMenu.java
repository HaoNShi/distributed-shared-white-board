package org.unimelb.whiteboard.client.menu;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.user.UserManager;
import org.unimelb.whiteboard.client.whiteboard.PaintManager;
import org.unimelb.whiteboard.client.whiteboard.WhiteBoardWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * File menu in the menu bar.
 */
public class FileMenu extends JMenu {

    public FileMenu(Client client, WhiteBoardWindow window, PaintManager paintManager, UserManager userManager) {
        super("File");
        this.setFont(new Font("File", Font.BOLD, 16));
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                paintManager.clearAll();
                client.setCurrentSavePath(null);
            }
        });
        this.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new FileOpenListener(window, client));
        this.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new FileSaveListener(window, client, "save"));
        this.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.addActionListener(new FileSaveListener(window, client, "saveAs"));
        this.add(saveAsMenuItem);

        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(closeMenuItem);

        if (paintManager.getMode() == PaintManager.CLIENT_MODE) {
            newMenuItem.setEnabled(false);
            openMenuItem.setEnabled(false);
            saveMenuItem.setEnabled(false);
            saveAsMenuItem.setEnabled(false);
            closeMenuItem.setEnabled(false);
        }

    }
}
