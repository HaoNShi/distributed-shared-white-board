package org.unimelb.whiteboard.client.Login;

import java.util.regex.Pattern;

public class LoginController {
    private final LoginView ui;
    protected String userId = "";
    protected String address = "";
    protected int port = -1;

    public LoginController(LoginView ui) {
        this.ui = ui;
    }

    public Boolean validateFormat() {
        Boolean checkId = false;
        Boolean checkAddress = false;
        Boolean checkPort = false;
        String idPatten = "^\\w{1,8}$";
        String portPatten = "^\\d+$";
        String addressPatten = "^.{1,20}$";
        // check userId
        userId = ui.userIdTextField.getText();
        if (!Pattern.matches(idPatten, userId)) {
            ui.lblIdWarn.setVisible(true);
            checkId = false;
        } else {
            ui.lblIdWarn.setVisible(false);
            checkId = true;
        }

        // check address
        address = ui.addressTextField.getText();
        if (!Pattern.matches(addressPatten, address)) {
            ui.lblAddressWarn.setVisible(true);
            checkAddress = false;
        } else {
            ui.lblAddressWarn.setVisible(false);
            checkAddress = true;
        }

        //check port
        String portStr = ui.portTextField.getText();
        if (!Pattern.matches(portPatten, portStr)) {
            ui.lblPortWarn.setVisible(true);
            checkPort = false;
        } else {
            try {
                port = Integer.parseInt(portStr);
                if (port <= 1024 || port >= 49151) {
                    ui.lblPortWarn.setVisible(true);
                    checkPort = false;
                } else {
                    ui.lblPortWarn.setVisible(false);
                    checkPort = true;
                }
            } catch (Exception e) {
                ui.lblPortWarn.setVisible(true);
                checkPort = false;
            }
        }
        return checkId && checkPort && checkAddress;
    }

}
