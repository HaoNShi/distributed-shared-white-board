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
    private final JTextField PasswordTextField;
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
        setBounds(100, 100, 300, 220);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JLabel lblRoomName = new JLabel("Room Name:");
        lblRoomName.setBounds(25, 22, 73, 20);
        contentPanel.add(lblRoomName);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(25, 67, 73, 20);
        contentPanel.add(lblPassword);

        roomNameTextField = new JTextField();
        roomNameTextField.setBounds(110, 22, 150, 25);
        roomNameTextField.setColumns(10);
        contentPanel.add(roomNameTextField);

        PasswordTextField = new JTextField();
        PasswordTextField.setBounds(110, 67, 150, 25);
        PasswordTextField.setColumns(10);
        contentPanel.add(PasswordTextField);

        JButton createBtn = new JButton("Create");
        createBtn.setBounds(110, 116, 60, 30);
        createBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                roomName = roomNameTextField.getText();
                password = PasswordTextField.getText();
                if (roomInfoCheck()) {
                    client.createRoom(roomName, password);
                    dispose();
                }
            }
        });
        createBtn.setActionCommand("OK");
        contentPanel.add(createBtn);
        getRootPane().setDefaultButton(createBtn);

//        JButton cancelBtn = new JButton("Cancel");
//        cancelBtn.setBounds(150, 116, 80, 30);
//        cancelBtn.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//            }
//        });
//        cancelBtn.setActionCommand("Cancel");
//        contentPanel.add(cancelBtn);
        
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
        String passwordPatten = "^\\w{0,8}$";

        if (!Pattern.matches(roomNamePatten, roomName)) {
            JOptionPane.showMessageDialog(this, "Room name contains illegal characters!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!Pattern.matches(passwordPatten, password)) {
            JOptionPane.showMessageDialog(this, "Password contains illegal characters!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public Client getClient() {
        return client;
    }
}
