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
        clients = new Vector<>();// Vector是clients的集合，是线程安全的！
        chatPanel.btnSend.addActionListener((e) -> {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            processMsg('[' + df.format(new Date()) + "] " + userId + ":\n" + this.chatPanel.txtInput.getText());
        });
    }

    // 处理信息：1.把信息显示到列表框里。2.广播信息
    public void processMsg(String str) {
        SwingUtilities.invokeLater(() -> chatPanel.textArea.append(str + '\n'));

        broadcastMsg(str);
    }

    // 广播：用一个循环把信息发给所有连接
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
                System.err.println("A guest exit in an abnormal way");
                // e.printStackTrace();
            }

        }
    }

    void btnSend_actionPerformed(ActionEvent e) {
        broadcastMsg(chatPanel.txtInput.getText());
    }

    // Create a ServerSocket to listen for connections on; start the thread
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
        // 这个线程是server thread，处理多个客户，下面的run()函数就是这个线程的
        thread = new Thread(this);
        thread.start();
    }

    // The body of the server thread.(上面的thread)
    // Loop forever,listening for and accepting connections from clients.
    // For each connection, create a Connection object to handle communication
    // through the new Socket.
    public void run() {
        try {
            while (true) {
                Socket client_socket = listen_socket.accept();
                // 封装成功能更强的Connection对象
                Connection c = new Connection(client_socket, this);
                clients.add(c);
                processMsg("A new guest Comes in");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
