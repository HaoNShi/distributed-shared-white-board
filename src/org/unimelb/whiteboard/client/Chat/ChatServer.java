package org.unimelb.whiteboard.client.Chat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

public class ChatServer implements Runnable {
    private final String userId;
    protected ServerSocket listen_socket;
    private ChatPanel chatPanel;
    private Thread thread;
    private Vector<Connection> clients;
    private int chatPort;

    public ChatServer(String userId) {
        this.userId = userId;
        try {
            init();
            ServerListen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return chatPort;
    }

    public ChatPanel getPanel() {
        return chatPanel;
    }

    private void init() {
        chatPanel = new ChatPanel();
        clients = new Vector<>(); // Vector is a collection of clients and is thread-safe
        chatPanel.btnSend.addActionListener((e) -> {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            processMsg('[' + df.format(new Date()) + "] " + userId + ":\n" + this.chatPanel.txtInput.getText());
        });
    }

    /**
     * Processing information: 1. Display the information in the textArea. 2. Broadcast information
     */
    public void processMsg(String str) {
        SwingUtilities.invokeLater(() -> chatPanel.textArea.append(str + '\n'));
        broadcastMsg(str);
    }

    /**
     * Broadcast: send information to all connections in a loop
     */
    public void broadcastMsg(String str) {
        Iterator<Connection> iter = clients.iterator();
        Connection client;
        while (iter.hasNext()) {
            client = iter.next();
            try {
                client.sendMsg(str);
            } catch (Exception e) {
                client.disconnect();
                iter.remove();
                System.err.println("A member exit in an abnormal way.");
            }
        }
    }

    void btnSend_actionPerformed(ActionEvent e) {
        broadcastMsg(chatPanel.txtInput.getText());
    }

    /**
     * Create a ServerSocket to listen for connections on; start the thread
     */
    public void ServerListen() {
        try {
            // Get a random chat port (Available one).
            ServerSocket tempSocket = new ServerSocket(0);
            chatPort = tempSocket.getLocalPort();
            tempSocket.close();

            listen_socket = new ServerSocket(chatPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        processMsg("Chat: listening on port " + chatPort);
        // Server thread, which handles multiple clients, run below is for this thread
        thread = new Thread(this);
        thread.start();
    }

    /**
     * The body of the server thread.(thread above)
     * Loop forever,listening for and accepting connections from clients.
     * For each connection, create a Connection object to handle communication through the new Socket.
     */
    public void run() {
        try {
            while (true) {
                Socket client_socket = listen_socket.accept();
                // 封装成功能更强的Connection对象
                Connection c = new Connection(client_socket, this);
                clients.add(c);
                processMsg("A new member comes in");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
