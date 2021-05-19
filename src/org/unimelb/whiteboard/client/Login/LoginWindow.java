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
    protected JLabel lblIdWarn = null;
    protected JLabel lblAddressWarn = null;
    protected JLabel lblPortWarn = null;
    protected Client client;
    private JFrame frame;
    private LoginController controller;
    private JLabel tipsLabel;

    /**
     * Create the application.
     */
    public LoginWindow(Client client) {
        this.client = client;
        initialize();
        this.controller = new LoginController(this);
    }

    /**
     * Get the frame
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Set the text in the tips label.
     */
    public void setTipsLabel(String text) {
        tipsLabel.setText(text);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 366, 259);
        frame.setTitle("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        btnLogin.setBounds(122, 162, 106, 32);
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

        JLabel lblServerAddress = new JLabel("Address:");
        lblServerAddress.setFont(new Font("Arial", Font.PLAIN, 12));
        lblServerAddress.setBounds(43, 45, 58, 16);
        frame.getContentPane().add(lblServerAddress);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPort.setBounds(43, 77, 58, 16);
        frame.getContentPane().add(lblPort);

        JLabel lblUserId = new JLabel("User ID:");
        lblUserId.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUserId.setBounds(43, 116, 58, 16);
        frame.getContentPane().add(lblUserId);

        addressTextField = new JTextField();
        addressTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        addressTextField.setBounds(111, 40, 130, 26);
        addressTextField.setText(client.getIp());
        addressTextField.setColumns(10);
        frame.getContentPane().add(addressTextField);

        portTextField = new JTextField();
        portTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        portTextField.setText("4443");
        portTextField.setBounds(111, 72, 129, 26);
        portTextField.setColumns(10);
        frame.getContentPane().add(portTextField);

        userIdTextField = new JTextField();
        userIdTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        userIdTextField.setBounds(111, 111, 130, 26);
        userIdTextField.setDocument(new NumberTextField(8, false));
        userIdTextField.setColumns(8);
        frame.getContentPane().add(userIdTextField);

        lblAddressWarn = new JLabel("Invalid!");
        lblAddressWarn.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAddressWarn.setBounds(247, 45, 46, 16);
        lblAddressWarn.setForeground(Color.RED);
        lblAddressWarn.setVisible(false);
        frame.getContentPane().add(lblAddressWarn);

        lblPortWarn = new JLabel("Invalid!");
        lblPortWarn.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPortWarn.setBounds(247, 77, 46, 16);
        lblPortWarn.setForeground(Color.RED);
        lblPortWarn.setVisible(false);
        frame.getContentPane().add(lblPortWarn);

        lblIdWarn = new JLabel("A-Za-z0-9_");
        lblIdWarn.setFont(new Font("Arial", Font.PLAIN, 12));
        lblIdWarn.setBounds(247, 116, 78, 16);
        lblIdWarn.setForeground(Color.RED);
        lblIdWarn.setVisible(false);
        frame.getContentPane().add(lblIdWarn);
    }
}
