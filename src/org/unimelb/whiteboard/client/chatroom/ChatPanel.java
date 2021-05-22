package org.unimelb.whiteboard.client.chatroom;

import javax.swing.*;
import java.awt.*;


public class ChatPanel extends JPanel {
    private static final long serialVersionUID = 4162325845732304245L;
    protected JTextArea textArea;
    JTextField txtInput;
    JButton btnSend;

    public ChatPanel() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendText(String text) {
        textArea.append(text);
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));
        JScrollPane chatScrollPane = new JScrollPane();
        chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(chatScrollPane, BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        chatScrollPane.setViewportView(textArea);

        JPanel pnlFoot = new JPanel();
        pnlFoot.setLayout(new BorderLayout(0, 0));
        txtInput = new JTextField("", 10);
        pnlFoot.add(txtInput, BorderLayout.CENTER);

        btnSend = new JButton("Send");
        btnSend.setPreferredSize(new Dimension(70, 0));
        pnlFoot.add(btnSend, BorderLayout.EAST);

        add(pnlFoot, BorderLayout.SOUTH);
    }
}
