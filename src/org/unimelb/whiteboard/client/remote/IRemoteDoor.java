package org.unimelb.whiteboard.client.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IRemoteDoor extends Remote {
    void knock(String userId, String ip, int registerPort) throws RemoteException;

    void cancelKnock(String userId) throws RemoteException;

    void leave(String userId) throws RemoteException;
}
