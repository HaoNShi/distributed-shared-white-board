package org.unimelb.whiteboard.client.RMI;

import org.unimelb.whiteboard.client.Shape.MyShape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;


public interface IRemotePaint extends Remote {
    void addShape(MyShape shape) throws RemoteException;

    void setHistory(Vector<MyShape> paintHistory) throws RemoteException;

    void clearHistory() throws RemoteException;
}
