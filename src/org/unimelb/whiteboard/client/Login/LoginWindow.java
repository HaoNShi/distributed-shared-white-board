package org.unimelb.whiteboard.client.Login;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.StateCode.StateCode;
import org.unimelb.whiteboard.util.NumberTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow {
    protected JTextField userIdTextField;
    protected JTextField addressTextField;
    protected JTextField portTextField;
    protected Client client;
    private JFrame frame;
    private final LoginController controller;

    /**
     * Create the application.
     */
    public LoginWindow(Client client) {
        this.client = client;
        initialize();
        this.controller = new LoginController(this);
    }

    /**
     * Get the frame.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 334, 224);
        frame.setTitle("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(120, 135, 80, 30);
        frame.getContentPane().add(btnLogin);
        // Login logic here.
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.validateFormat()) {
                    // The function above is asynchronous. Use the invokeLater to keep it work synchronous.
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                client.setUserId(controller.userId);
                                client.setServerIp(controller.address);
                                client.setServerPort(controller.port);
                                int state = client.register();
                                if (state == StateCode.SUCCESS) {
                                    client.switch2Lobby();
                                } else if (state == StateCode.FAIL) {
                                    JOptionPane.showMessageDialog(frame, "User name exist! Change one!", "Warning",
                                            JOptionPane.WARNING_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Can not connect to the server!", "Warning",
                                            JOptionPane.WARNING_MESSAGE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAddress.setBounds(28, 29, 58, 16);
        frame.getContentPane().add(lblAddress);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPort.setBounds(28, 65, 58, 16);
        frame.getContentPane().add(lblPort);

        JLabel lblUserId = new JLabel("User ID:");
        lblUserId.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUserId.setBounds(28, 100, 58, 16);
        frame.getContentPane().add(lblUserId);

        addressTextField = new JTextField();
        addressTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        addressTextField.setBounds(96, 24, 192, 26);
        addressTextField.setText(client.getIp());
        addressTextField.setColumns(10);
        frame.getContentPane().add(addressTextField);

        portTextField = new JTextField();
        portTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        portTextField.setText("4443");
        portTextField.setBounds(96, 60, 192, 26);
        portTextField.setColumns(10);
        frame.getContentPane().add(portTextField);

        userIdTextField = new JTextField();
        userIdTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        userIdTextField.setBounds(96, 95, 192, 26);
        userIdTextField.setDocument(new NumberTextField(8, false));
        userIdTextField.setColumns(8);
        frame.getContentPane().add(userIdTextField);
    }
}
