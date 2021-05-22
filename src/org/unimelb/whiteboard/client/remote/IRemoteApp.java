package org.unimelb.whiteboard.client.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IRemoteApp extends Remote {
    /**
     * use to ask the client who has knock the door of host to come in.
     */
    void askIn(String hostIp, int chatPort) throws RemoteException;

    void askOut() throws RemoteException;
}
