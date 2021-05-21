package org.unimelb.whiteboard.client.RMI;

import org.unimelb.whiteboard.client.User.UserManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RemoteDoor extends UnicastRemoteObject implements IRemoteDoor {

    UserManager userManager;

    public RemoteDoor(UserManager userManager) throws RemoteException {
        this.userManager = userManager;
    }

    @Override
    public void knock(String userId, String ip, int registerPort) throws RemoteException {
        System.out.println("A applicant knocks the door.");
        userManager.addApplicant(userId, ip, registerPort);
    }

    @Override
    public void cancelKnock(String userId) throws RemoteException {
        System.out.println("A applicant leaves.");
        userManager.removeApplicant(userId);
    }

    @Override
    public void leave(String userId) throws RemoteException {
        System.out.println("A member leaves.");
        userManager.removeMember(userId);
    }
}
