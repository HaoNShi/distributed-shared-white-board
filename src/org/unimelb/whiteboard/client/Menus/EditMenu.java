package org.unimelb.whiteboard.client.Menus;

import org.unimelb.whiteboard.client.WhiteBoard.PaintManager;
import org.unimelb.whiteboard.client.WhiteBoard.WhiteBoardWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Edit menu in the menu bar.
 */
public class EditMenu extends JMenu {
    private final WhiteBoardWindow wbv;
    private final PaintManager paintManager;
    private final JMenuItem redoItem;
    private final JMenuItem undoItem;

    public EditMenu(WhiteBoardWindow wbv) {
        super("Edit(E)");
        this.wbv = wbv;
        paintManager = wbv.getPaintManager();
        this.setMnemonic(KeyEvent.VK_E);

        undoItem = new JMenuItem("Undo", KeyEvent.VK_Z);

        // Check whether the operate system is Mac OS, decide use ctrl or command
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        }
        undoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (paintManager.getMode() == PaintManager.SERVER_MODE) {
                    wbv.getPaintManager().undo();
                    undoItem.setEnabled(paintManager.isUndoAllow());
                }
            }
        });
        this.add(undoItem);

        redoItem = new JMenuItem("Redo");
        if (osName.startsWith("Mac OS")) {
            redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | ActionEvent.SHIFT_MASK));
        } else {
            redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        }
        redoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (paintManager.getMode() == PaintManager.SERVER_MODE) {
                    wbv.getPaintManager().redo();
                    redoItem.setEnabled(paintManager.isRedoAllow());
                }
            }
        });
        this.add(redoItem);
        updateEnable();
    }

    /**
     * Set whether redo is able.
     */
    public void setRedoEnable(Boolean isAble) {
        if (wbv.getPaintManager().getMode() == PaintManager.SERVER_MODE) {
            redoItem.setEnabled(isAble);
        }
    }

    /**
     * Set whether undo is able.
     */
    public void setUndoEnable(Boolean isAble) {
        if (wbv.getPaintManager().getMode() == PaintManager.SERVER_MODE) {
            undoItem.setEnabled(isAble);
        }
    }

    /**
     * Update the undo and redo button's isEnable.
     */
    public void updateEnable() {
        if (paintManager.getMode() == PaintManager.CLIENT_MODE) {
            undoItem.setEnabled(false);
        } else {
            undoItem.setEnabled(paintManager.isUndoAllow());
        }

        if (paintManager.getMode() == PaintManager.CLIENT_MODE) {
            redoItem.setEnabled(false);
        } else {
            redoItem.setEnabled(paintManager.isRedoAllow());
        }
    }
}
