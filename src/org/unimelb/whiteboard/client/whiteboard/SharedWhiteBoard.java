package org.unimelb.whiteboard.client.whiteboard;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.remote.IRemotePaint;
import org.unimelb.whiteboard.client.remote.IRemoteUM;
import org.unimelb.whiteboard.client.remote.RemotePaint;
import org.unimelb.whiteboard.client.remote.RemoteUM;
import org.unimelb.whiteboard.client.user.UserManager;


public abstract class SharedWhiteBoard {
    protected PaintManager paintManager;
    protected UserManager userManager;
    protected WhiteBoardWindow ui;
    protected Client client;
    private IRemotePaint remotePaint;
    private IRemoteUM remoteUM;

    // Room ID is unique in central server.
    private int roomId;

    public SharedWhiteBoard(Client client) {
        this.client = client;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public WhiteBoardWindow getWindow() {
        return ui;
    }

    protected void initPaintRMI() {
        try {
            remotePaint = new RemotePaint(paintManager);
            client.getRegistry().bind("paint", remotePaint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initUmRMI() {
        try {
            remoteUM = new RemoteUM(userManager);
            client.getRegistry().bind("um", remoteUM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
