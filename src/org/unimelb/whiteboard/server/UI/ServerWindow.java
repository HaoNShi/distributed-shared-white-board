package org.unimelb.whiteboard.server.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ServerWindow {

    private JFrame frame;
    private JTextArea logArea;
    private JScrollPane scrollPane;
    private JLabel addressLabel;

    /**
     * Create the application.
     */
    public ServerWindow(String address, String port) {
        initialize(address, port);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextArea getLogArea() {
        return logArea;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(String address, String port) {
        frame = new JFrame();
        frame.setTitle("Server");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.out.println("Server Close!");
            }
        });

        addressLabel = new JLabel("<html><body>Address: " + address + "<br>Port: " + port + "</body></html>");
        addressLabel.setBounds(20, 20, 400, 30);
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.getContentPane().add(addressLabel);

        logArea = new JTextArea();
        logArea.setLineWrap(true);
        logArea.setEditable(false);
        scrollPane = new JScrollPane(logArea);
        scrollPane.setBounds(20, 80, 400, 170);
        frame.getContentPane().add(scrollPane);
    }
}
