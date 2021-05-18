package org.unimelb.whiteboard.util;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.ClientUser.UserManager;
import org.unimelb.whiteboard.client.WhiteBoard.PaintManager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class WhiteBoardCloseListener extends WindowAdapter {

    private final Client client;
    private final PaintManager paintManager;
    private final UserManager userManager;

    public WhiteBoardCloseListener(Client client, PaintManager paintManager, UserManager userManager) {
        this.client = client;
        this.paintManager = paintManager;
        this.userManager = userManager;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);

        if (paintManager.getMode() == PaintManager.CLIENT_MODE) {
            try {
                client.getTempRemoteDoor().leave(client.getUserId());
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        } else {
            userManager.clear();
        }
        client.removeUser();
    }
}
