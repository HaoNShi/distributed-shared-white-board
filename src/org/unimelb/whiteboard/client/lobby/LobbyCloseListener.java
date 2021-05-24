package org.unimelb.whiteboard.client.lobby;

import org.unimelb.whiteboard.client.Client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Use when close in lobby.
 */
public class LobbyCloseListener extends WindowAdapter {

    private final Client client;

    public LobbyCloseListener(Client client) {
        this.client = client;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        client.removeUser();
    }
}
