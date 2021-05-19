package org.unimelb.whiteboard.client.WhiteBoard;

import org.unimelb.whiteboard.client.Chat.ChatServer;
import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.RMI.IRemoteDoor;
import org.unimelb.whiteboard.client.RMI.RemoteDoor;
import org.unimelb.whiteboard.client.User.UserManager;

public class ServerWhiteBoard extends SharedWhiteBoard {

    private IRemoteDoor remoteDoor;
    private ChatServer chatServer;

    public ServerWhiteBoard(Client client) {
        super(client);
        initManager();
        initDoorRMI();
        super.initUmRMI();
        super.initPaintRMI();
        userManager.setHostPaintManager(paintManager);
        initChat();
        initView();
    }

    public void initView() {
        String title = "White Board-" + client.getIp() + ":" + client.getRegistryPort();
        ui = new WhiteBoardWindow(client, this.paintManager, userManager, title);
        ui.setChatPanel(chatServer.getPanel());
    }

    private void initDoorRMI() {
        try {
            remoteDoor = new RemoteDoor(userManager);
            client.getRegistry().bind("door", remoteDoor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initManager() {
        userManager = new UserManager(true, client.getUserId(), client.getIp(), client.getRegistryPort(), -1);
        paintManager = new PaintManager(PaintManager.SERVER_MODE, userManager);
    }

    private void initChat() {
        chatServer = new ChatServer(client.getUserId());
        userManager.setHostChatPort(chatServer.getPort());
        userManager.setChatPanel(chatServer.getPanel());
    }

}
