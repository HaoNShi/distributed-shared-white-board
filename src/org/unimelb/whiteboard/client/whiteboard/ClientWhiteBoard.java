package org.unimelb.whiteboard.client.whiteboard;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.chatroom.ChatClient;
import org.unimelb.whiteboard.client.user.UserManager;

public class ClientWhiteBoard extends SharedWhiteBoard {
    private ChatClient chatClient;

    public ClientWhiteBoard(Client client, String hostId, String hostIp, int registerPort) {
        super(client);
        initManager(hostId, hostIp, registerPort);
        initUmRMI();
        initPaintRMI();
        initWindow();
    }

    private void initWindow() {
        String title = "White Board";
        ui = new WhiteBoardWindow(client, this.paintManager, userManager, title);
    }

    public void createChatClient(String hostIp, int chatPort) {
        chatClient = new ChatClient(client.getUserId(), hostIp, chatPort);
        ui.setChatPanel(chatClient.getPanel());
    }

    private void initManager(String hostId, String hostIp, int registerPort) {
        userManager = new UserManager(false, hostId, hostIp, registerPort, -1);
        paintManager = new PaintManager(PaintManager.CLIENT_MODE, userManager);
    }

}