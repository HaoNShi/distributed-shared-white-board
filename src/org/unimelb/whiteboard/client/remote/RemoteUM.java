package org.unimelb.whiteboard.client.remote;

import org.unimelb.whiteboard.client.user.User;
import org.unimelb.whiteboard.client.user.UserManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;


public class RemoteUM extends UnicastRemoteObject implements IRemoteUM {
    UserManager um;

    public RemoteUM(UserManager um) throws RemoteException {
        this.um = um;
    }

    @Override
    public void setMembers(Map<String, User> members) throws RemoteException {
        System.out.println("Manager set the member list.");
        um.setMembers(members);
    }
}
