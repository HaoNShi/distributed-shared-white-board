package org.unimelb.whiteboard.client.RMI;

import org.unimelb.whiteboard.client.User.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;


public interface IRemoteUM extends Remote {
    void setMembers(Map<String, User> members) throws RemoteException;
}
