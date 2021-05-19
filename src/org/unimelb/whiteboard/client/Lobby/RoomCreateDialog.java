package org.unimelb.whiteboard.client.Lobby;

import org.unimelb.whiteboard.client.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;


public class RoomCreateDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private final JTextField roomNameTextField;
    private final JTextField PasswordTextField;
    private final Client client;
    private final JLabel lblError;
    private String roomName = "";
    private String password = "";

    /**
     * Create the dialog.
     */
    public RoomCreateDialog(Client client) {
        this.client = client;
        setTitle("Create Room Dialog");
        setResizable(false);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JLabel lblRoomName = new JLabel("Room Name:");
        lblRoomName.setBounds(99, 79, 106, 27);
        lblRoomName.setFont(new Font("Lucida Grande", Font.PLAIN, 15));

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(99, 133, 106, 27);
        lblPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 15));

        roomNameTextField = new JTextField();
        roomNameTextField.setBounds(211, 79, 150, 27);
        roomNameTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        roomNameTextField.setColumns(10);

        PasswordTextField = new JTextField();
        PasswordTextField.setBounds(211, 133, 150, 27);
        PasswordTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        PasswordTextField.setColumns(10);
        contentPanel.setLayout(null);
        contentPanel.add(lblRoomName);
        contentPanel.add(roomNameTextField);
        contentPanel.add(lblPassword);
        contentPanel.add(PasswordTextField);

        lblError = new JLabel("Error");
        lblError.setForeground(Color.RED);
        lblError.setVisible(false);
        lblError.setBounds(99, 183, 262, 16);

        contentPanel.add(lblError);
        JPanel buttonPane = new JPanel();
        buttonPane.setPreferredSize(new Dimension(0, 50));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        buttonPane.setLayout(new GridLayout(0, 2, 0, 0));
        {
            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    roomName = roomNameTextField.getText();
                    password = PasswordTextField.getText();
                    if (roomInfoCheck()) {
                        client.createRoom(roomName, password);
                        dispose();
                    }
                }
            });
            okButton.setActionCommand("OK");
            buttonPane.add(okButton);
            getRootPane().setDefaultButton(okButton);
        }
        {
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            cancelButton.setActionCommand("Cancel");
            buttonPane.add(cancelButton);
        }
    }

    public static void showCreateRoomDialog(Frame owner, Component parentComponent, Client client) {
        final RoomCreateDialog roomCreateDialog = new RoomCreateDialog(client);
        roomCreateDialog.setLocationRelativeTo(parentComponent);
        roomCreateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        roomCreateDialog.setModal(true);
        roomCreateDialog.setVisible(true);
    }

    private Boolean roomInfoCheck() {
        String roomNamePatten = "^\\w{1,8}$";
        String passwordPatten = "^\\w{0,8}$";
        String err;

        if (!Pattern.matches(roomNamePatten, roomName)) {
            err = "Room Name Format: \\w{1,8}";
            lblError.setText(err);
            lblError.setVisible(true);
            return false;
        }
        if (!Pattern.matches(passwordPatten, password)) {
            err = "Password Format: \\w{,8}";
            lblError.setText(err);
            lblError.setVisible(true);
            return false;
        }
        return true;
    }

    public Client getClient() {
        return client;
    }
}
