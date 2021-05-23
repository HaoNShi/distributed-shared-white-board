package org.unimelb.whiteboard.client.chatroom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class ChatClient implements Runnable {
    ChatPanel chatPanel;
    String userId;

    Socket socket;
    Thread thread;
    DataInputStream in;
    DataOutputStream out;
    boolean bConnected;

    int chatPort;
    String ip;

    public ChatClient(String userId, String ip, int chatPort) {
        this.userId = userId;
        this.ip = ip;
        this.chatPort = chatPort;
        try {
            init();
            startConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendEncrypted(String message, DataOutputStream out) {
        // Encrypt first
        String key = "5v8y/B?D(G+KbPeS";
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            // Perform encryption
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            out.writeUTF(Base64.getEncoder().encodeToString(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChatPanel getPanel() {
        return chatPanel;
    }

    private void init() {
        chatPanel = new ChatPanel();
        chatPanel.btnSend.addActionListener(e -> {
            if (chatPanel.txtInput.getText().length() != 0) {
                try {
                    sendMsg("[User] " + userId + ":\n" + this.chatPanel.txtInput.getText());
                    chatPanel.txtInput.setText("");
                } catch (IOException e2) {
                    processMsg(e2.toString());
                }
            }
        });
    }

    public void startConnect() {
        bConnected = false;
        try {
            socket = new Socket(ip, chatPort);
            bConnected = true;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void endConnect() {
        try {
            in.close();
            out.close();
            socket.close();
            thread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Continue to communicate with the server, including receiving and processing information
     */
    public void run() {
        while (true) {
            try {
                String msg = receiveMsg();
                Thread.sleep(100L);
                if (!msg.equals("")) {
                    processMsg(msg);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send information, write out stream to the server
     */
    public void sendMsg(String msg) throws IOException {
        sendEncrypted(msg, out);
        out.flush();
    }

    /**
     * Receive information, in stream
     */
    public String receiveMsg() throws IOException {
        try {
            if (in.available() > 0) {
                return in.readUTF();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Processing information (display information)
     */
    public void processMsg(String str) {
        SwingUtilities.invokeLater(() -> chatPanel.textArea.append(str + '\n'));
    }
}
