package org.unimelb.whiteboard.client.login;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.util.NumberTextField;
import org.unimelb.whiteboard.standard.StateCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow {
    private final LoginValidator validator;
    protected JTextField userIdTextField;
    protected JTextField addressTextField;
    protected JTextField portTextField;
    protected Client client;
    private JFrame frame;

    /**
     * Create the application.
     */
    public LoginWindow(Client client) {
        this.client = client;
        initialize();
        this.validator = new LoginValidator(this);
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
                if (validator.validateFormat()) {
                    // The function above is asynchronous. Use the invokeLater to keep it work synchronous.
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                client.setUserId(validator.userId);
                                client.setServerIp(validator.address);
                                client.setServerPort(validator.port);
                                int state = client.register();
                                if (state == StateCode.SUCCESS) {
                                    client.switch2Lobby();
                                } else if (state == StateCode.FAIL) {
                                    JOptionPane.showMessageDialog(frame, "User name exist! Change one!", "Warning",
                                            JOptionPane.WARNING_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(frame, "Can't connect to the server!", "Warning",
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
        lblAddress.setBounds(28, 29, 70, 16);
        frame.getContentPane().add(lblAddress);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(28, 65, 70, 16);
        frame.getContentPane().add(lblPort);

        JLabel lblUserId = new JLabel("User Name:");
        lblUserId.setBounds(28, 100, 70, 16);
        frame.getContentPane().add(lblUserId);

        addressTextField = new JTextField();
        addressTextField.setBounds(108, 24, 180, 26);
        addressTextField.setText(client.getIp());
        addressTextField.setColumns(10);
        frame.getContentPane().add(addressTextField);

        portTextField = new JTextField();
        portTextField.setText("8080");
        portTextField.setBounds(108, 60, 180, 26);
        portTextField.setColumns(10);
        frame.getContentPane().add(portTextField);

        userIdTextField = new JTextField();
        userIdTextField.setBounds(108, 95, 180, 26);
        userIdTextField.setDocument(new NumberTextField(8, false));
        userIdTextField.setColumns(8);
        frame.getContentPane().add(userIdTextField);
        frame.setLocationRelativeTo(null);
    }
}
