package org.unimelb.whiteboard.client.lobby;

import org.unimelb.whiteboard.client.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class RoomCreateWindow extends JDialog {
    private static final long serialVersionUID = 1953383401687653792L;
    private final JPanel contentPanel = new JPanel();
    private final JTextField roomNameTextField;
    private final Client client;
    private String roomName = "";
    private String password = "";

    /**
     * Create the dialog.
     */
    public RoomCreateWindow(Client client) {
        this.client = client;
        setTitle("New Room");
        setResizable(false);
        setBounds(100, 100, 305, 164);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JLabel lblRoomName = new JLabel("Room Name:");
        lblRoomName.setBounds(27, 29, 73, 20);
        contentPanel.add(lblRoomName);

        roomNameTextField = new JTextField();
        roomNameTextField.setBounds(111, 27, 150, 25);
        roomNameTextField.setColumns(10);
        contentPanel.add(roomNameTextField);

        JButton createBtn = new JButton("Create");
        createBtn.setBounds(111, 72, 73, 30);
        createBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                roomName = roomNameTextField.getText();
                password = "";
                if (roomInfoCheck()) {
                    client.createRoom(roomName, password);
                    dispose();
                }
            }
        });
        createBtn.setActionCommand("OK");
        contentPanel.add(createBtn);
        getRootPane().setDefaultButton(createBtn);
        
    }

    public static void showCreateRoomDialog(Frame owner, Component parentComponent, Client client) {
        final RoomCreateWindow roomCreateWindow = new RoomCreateWindow(client);
        roomCreateWindow.setLocationRelativeTo(parentComponent);
        roomCreateWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        roomCreateWindow.setModal(true);
        roomCreateWindow.setVisible(true);
    }

    private Boolean roomInfoCheck() {
        String roomNamePatten = "^\\w{1,8}$";

        if (!Pattern.matches(roomNamePatten, roomName)) {
            JOptionPane.showMessageDialog(this, "Room name type is String!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public Client getClient() {
        return client;
    }
}
