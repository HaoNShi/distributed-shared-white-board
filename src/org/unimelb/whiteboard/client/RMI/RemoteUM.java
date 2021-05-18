package org.unimelb.whiteboard.client.RMI;

import org.unimelb.whiteboard.client.ClientUser.User;
import org.unimelb.whiteboard.client.ClientUser.UserManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;


public class RemoteUM extends UnicastRemoteObject implements IRemoteUM {
    UserManager um;

    public RemoteUM(UserManager um) throws RemoteException {
        this.um = um;
    }

    @Override
    public void setGuests(Map<String, User> guest) throws RemoteException {
        System.out.println("Host set the guest list.");
        um.setGuests(guest);
    }
}
