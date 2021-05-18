package org.unimelb.whiteboard.client.RMI;

import org.unimelb.whiteboard.client.ClientUser.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;


public interface IRemoteUM extends Remote {
    void setGuests(Map<String, User> guest) throws RemoteException;
}
