package org.unimelb.whiteboard.client.remote;

import org.unimelb.whiteboard.client.shape.MyShape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;


public interface IRemotePaint extends Remote {
    void addShape(MyShape shape) throws RemoteException;

    void setHistory(Vector<MyShape> paintHistory) throws RemoteException;

    void clearHistory() throws RemoteException;
}
