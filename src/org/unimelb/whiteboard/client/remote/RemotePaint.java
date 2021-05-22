package org.unimelb.whiteboard.client.remote;

import org.unimelb.whiteboard.client.shape.MyShape;
import org.unimelb.whiteboard.client.whiteboard.PaintManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;


public class RemotePaint extends UnicastRemoteObject implements IRemotePaint {
    private static final long serialVersionUID = 1L;
    // All the painting action should be do by the paintManager.
    PaintManager paintManager;

    public RemotePaint(PaintManager paintManager) throws RemoteException {
        this.paintManager = paintManager;
    }

    @Override
    public void addShape(MyShape shape) throws RemoteException {
        System.out.println("Member add shape.");
        paintManager.addShape(shape);
    }

    @Override
    public void setHistory(Vector<MyShape> paintHistory) throws RemoteException {
        paintManager.setPaintHistory(paintHistory);
        paintManager.getPaintArea().repaint();
    }

    @Override
    public void clearHistory() throws RemoteException {
        paintManager.resetAll();
    }
}
