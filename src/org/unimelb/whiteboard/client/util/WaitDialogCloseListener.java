package org.unimelb.whiteboard.client.util;

import org.unimelb.whiteboard.client.lobby.LobbyController;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WaitDialogCloseListener extends WindowAdapter {
    private final LobbyController controller;

    public WaitDialogCloseListener(LobbyController controller) {
        this.controller = controller;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        controller.cancelKnock();
    }
}

