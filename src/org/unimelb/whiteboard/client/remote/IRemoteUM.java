package org.unimelb.whiteboard.client.remote;

import org.unimelb.whiteboard.client.user.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;


public interface IRemoteUM extends Remote {
    void setMembers(Map<String, User> members) throws RemoteException;
}
