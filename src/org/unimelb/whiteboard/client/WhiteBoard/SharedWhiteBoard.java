package org.unimelb.whiteboard.client.WhiteBoard;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.ClientUser.UserManager;
import org.unimelb.whiteboard.client.RMI.IRemotePaint;
import org.unimelb.whiteboard.client.RMI.IRemoteUM;
import org.unimelb.whiteboard.client.RMI.RemotePaint;
import org.unimelb.whiteboard.client.RMI.RemoteUM;


public abstract class SharedWhiteBoard {
    protected PaintManager paintManager;
    protected UserManager userManager;
    protected WhiteBoardView ui;
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

    public WhiteBoardView getView() {
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
