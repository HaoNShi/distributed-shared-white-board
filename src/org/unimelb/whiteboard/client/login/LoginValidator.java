package org.unimelb.whiteboard.client.login;

import javax.swing.*;
import java.util.regex.Pattern;

public class LoginValidator {
    private final LoginWindow ui;
    protected String userId = "";
    protected String address = "";
    protected int port = -1;

    public LoginValidator(LoginWindow ui) {
        this.ui = ui;
    }

    public Boolean validateFormat() {
        String addressPatten = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
        String portPatten = "^\\d+$";
        String idPatten = "^\\w{1,8}$";

        // check address
        address = ui.addressTextField.getText();
        if (!Pattern.matches(addressPatten, address)) {
            JOptionPane.showMessageDialog(ui.getFrame(), "Address incorrect", "Warning", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // check port
        String portStr = ui.portTextField.getText();
        if (!Pattern.matches(portPatten, portStr)) {
            JOptionPane.showMessageDialog(ui.getFrame(), "Port incorrect", "Warning", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            try {
                port = Integer.parseInt(portStr);
                if (port <= 1024 || port >= 49151) {
                    JOptionPane.showMessageDialog(ui.getFrame(), "Port out of bounds!", "Warning", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ui.getFrame(), "Port incorrect", "Warning", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        // check userId
        userId = ui.userIdTextField.getText();
        if (!Pattern.matches(idPatten, userId)) {
            JOptionPane.showMessageDialog(ui.getFrame(), "User name should not be blank", "Warning", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
