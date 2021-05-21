package org.unimelb.whiteboard.client.WhiteBoard;

import org.unimelb.whiteboard.client.Menus.EditMenu;
import org.unimelb.whiteboard.client.RMI.IRemotePaint;
import org.unimelb.whiteboard.client.Shape.MyShape;
import org.unimelb.whiteboard.client.User.UserManager;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;


public class PaintManager {
    // Default mode
    public static final int SERVER_MODE = 0;
    public static final int CLIENT_MODE = 1;
    public static final int OFFLINE_MODE = 2;
    // User manager
    private final UserManager userManager;
    // There are three kind of mode: server, client and offline
    private final int mode;
    // Use to record all the shapes
    private Vector<MyShape> paintHistory;
    // Use to handle redo and undo, only host can use
    private Stack<MyShape> redoHistory = null;
    // Use to store the current painting area
    private PaintBoardPanel paintArea;
    // Use to control the editMenu UI
    private EditMenu editMenu;

    /**
     * @param mode There are three kind of mode: server, client and offline
     */
    public PaintManager(int mode, UserManager userManager) {
        this.mode = mode;
        this.userManager = userManager;
        paintHistory = new Vector<>();
        if (mode == SERVER_MODE)
            redoHistory = new Stack<>();
    }

    /**
     * Use to get the paint area.
     *
     * @return The current PaintBoardPanel object.
     */
    public PaintBoardPanel getPaintArea() {
        return paintArea;
    }

    /**
     * Set the current painting area.
     *
     * @param paintArea the PaintBoardPanel where you want to paint.
     */
    public void setPaintArea(PaintBoardPanel paintArea) {
        this.paintArea = paintArea;
    }

    /**
     * Remove all things inside the current painting area.
     */
    public void resetAll() {
        paintHistory.clear();
        if (mode == SERVER_MODE) {
            redoHistory.clear();
            if (editMenu != null) editMenu.updateEnable();
        }
        paintArea.removeAll();
        paintArea.revalidate();
        paintArea.repaint();
    }

    /**
     * Use to add shape to the history. Would affect both server and client.
     *
     * @param shape a MyShape object which you want to add to the current painting area
     */
    public synchronized void addShape(MyShape shape) {
        if (mode == SERVER_MODE) {
            paintHistory.add(shape);
            redoHistory.clear();
            if (editMenu != null) editMenu.updateEnable();
            Map<String, IRemotePaint> memberRemotePaints = userManager.getMemberRemotePaints();
            for (String userId : memberRemotePaints.keySet()) {
                try {
                    updateRemoteHistory(memberRemotePaints.get(userId));
                } catch (RemoteException e) {
                    userManager.removeMember(userId);
                    System.err.println("Can't connect to member " + userId + ", Remove.");
                }
            }
            paintArea.repaint();
        } else if (mode == CLIENT_MODE) {
            try {
                userManager.getManagerRemotePaint().addShape(shape);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set a specific remoteHistory to current paint history.
     */
    public void updateRemoteHistory(IRemotePaint remotePaint) throws RemoteException {
        remotePaint.setHistory(paintHistory);
    }

    /**
     * Use to clear the history, which would affect both server and client.
     */
    public void clearAll() {
        paintArea.clearBuffer();
        if (mode == SERVER_MODE) {
            paintHistory.clear();
            redoHistory.clear();
            if (editMenu != null) editMenu.updateEnable();
            Map<String, IRemotePaint> memberRemotePaints = userManager.getMemberRemotePaints();
            for (IRemotePaint x : memberRemotePaints.values()) {
                try {
                    x.clearHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            paintArea.repaint();
        } else if (mode == CLIENT_MODE) {
            System.out.println("Err: No clear operation access!");
        }
    }

    /**
     * Get the painting history.
     *
     * @return paint history.
     */
    public Vector<MyShape> getPaintHistory() {
        return paintHistory;
    }

    /**
     * Set the painting history.
     */
    public void setPaintHistory(Vector<MyShape> paintHistory) {
        this.paintHistory = paintHistory;

        if (mode == SERVER_MODE) {
            if (editMenu != null) editMenu.updateEnable();
            Map<String, IRemotePaint> memberRemotePaints = userManager.getMemberRemotePaints();
            for (String userId : memberRemotePaints.keySet()) {
                try {
                    updateRemoteHistory(memberRemotePaints.get(userId));
                } catch (RemoteException e) {
                    userManager.removeMember(userId);
                }
            }
        }

        paintArea.repaint();
    }

    /**
     * Get whether the manager belongs to a client or
     */
    public int getMode() {
        return mode;
    }

    /**
     * Undo, only host can use.
     */
    public void undo() {
        if (mode == SERVER_MODE) {
            redoHistory.push(paintHistory.lastElement());
            paintHistory.remove(paintHistory.size() - 1);
            setPaintHistory(paintHistory);
        }
    }

    /**
     * Redo, only host can use.
     */
    public void redo() {
        if (mode == SERVER_MODE) {
            paintHistory.add(redoHistory.pop());
            setPaintHistory(paintHistory);
        }
    }

    /**
     * Check whether undo is allowed.
     */
    public Boolean isUndoAllow() {
        if (mode == SERVER_MODE) {
            return !paintHistory.isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Check whether redo is allowed.
     */
    public Boolean isRedoAllow() {
        if (mode == SERVER_MODE) {
            return !redoHistory.isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Set the editMenu UI.
     */
    public void setEditMenu(EditMenu editMenu) {
        this.editMenu = editMenu;
    }

    public void clearRedoHistory() {
        if (redoHistory != null) redoHistory.clear();
    }
}
