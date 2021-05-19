package org.unimelb.whiteboard.client.Lobby;

import com.alibaba.fastjson.JSONObject;
import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.RMI.IRemoteDoor;
import org.unimelb.whiteboard.client.StateCode.StateCode;
import org.unimelb.whiteboard.util.Execute;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;


public class LobbyController {
    private final Client client;
    private final LobbyWindow ui;

    private String tempHostId;
    private String tempHostIp;
    private int tempHostRegistorPort;

    public LobbyController(Client client, LobbyWindow ui) {
        this.client = client;
        this.ui = ui;
    }

    /**
     * Get rooms list from central server.
     */
    public void refreshRoomsList() {
        // sent request to central server to gain roomlist.
        client.pullRemoteRoomList();

        // repaint the roomlist panel.
        ui.roomsBtnVec.clear();
        reFreshRoomsListPanel();

        int i = 0;
        JPanel currentPanel = ui.firstPanel;
        for (Map.Entry<Integer, String> entry : client.roomList.entrySet()) {
            JButton tempBtn = new JButton();
            String[] roomInfo = entry.getValue().split(" ");
            String roomName = roomInfo[0];
            String hostName = roomInfo[1];
            tempBtn.setText(roomName + " - " + hostName);
            ImageIcon joinIcon = new ImageIcon(ui.joinImagePath);
            joinIcon.setImage(joinIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            tempBtn.setIcon(joinIcon);
            tempBtn.addActionListener(new ActionListener() {
                /**
                 * Try to join a exist room.
                 */
                public void actionPerformed(ActionEvent e) {
                    String password = JOptionPane.showInputDialog(ui.frame, "Please Enter Password:",
                            "Room: " + roomName, JOptionPane.OK_CANCEL_OPTION);
                    if (password != null) {
                        // So wired! Should learn more about entrySet().
                        int roomId = Integer.parseInt(entry.getKey() + "");
                        JSONObject reqJSON = new JSONObject();
                        reqJSON.put("command", StateCode.GET_ROOM_INFO);
                        reqJSON.put("roomId", roomId);
                        reqJSON.put("password", password);
                        JSONObject resJSON = Execute.execute(reqJSON, client.getServerIp(), client.getServerPort());
                        int state = resJSON.getInteger("state");
                        if (state == StateCode.SUCCESS) {
                            tempHostId = resJSON.getString("hostId");
                            tempHostIp = resJSON.getString("ip");
                            tempHostRegistorPort = resJSON.getInteger("port");
                            // When create here, the window's position right.
                            ui.createWaitDialog();
                            System.out.println("Klock the host's door.");
                            try {
//								Registry registry = LimitedTimeRegistry.getLimitedTimeRegistry(tempHostIp, tempHostRegistorPort, 1000);
                                Registry registry = LocateRegistry.getRegistry(tempHostIp, tempHostRegistorPort);
                                client.setTempRemoteDoor((IRemoteDoor) registry.lookup("door"));
                                client.createTempClientWhiteBoard(tempHostId, tempHostIp, tempHostRegistorPort);
                                client.getTempRemoteDoor().knock(client.getUserId(), client.getIp(), client.getRegistryPort());
                                // The follow code would block all code.
                                ui.setWaitDialogVisible(true);
                            } catch (Exception exception) {
                                client.unbindAndSetNull();
                                System.out.println("The host's network has problem!");
                                JOptionPane.showMessageDialog(ui.getFrame(), "The host's network has problem!");
                                //exception.printStackTrace();
                            }
                        } else if (state == StateCode.FAIL) {
                            JOptionPane.showMessageDialog(ui.frame, "Password wrong or the room is removed, please refresh!", "Warning",
                                    JOptionPane.WARNING_MESSAGE);
                        } else if (state != StateCode.SUCCESS) {
                            JOptionPane.showMessageDialog(ui.frame, "Can not connect to central server!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            ui.roomsBtnVec.add(tempBtn);

            if (i % 2 != 0) {
                ui.roomsListPanel.setPreferredSize(new Dimension(0, (i / 2 + 2) * 170));
                JPanel temp = new JPanel();
                temp.setBounds(5, (i / 2 + 1) * 170, 570, 160);
                temp.setLayout(new GridLayout(1, 2, 5, 0));
                currentPanel = temp;
                ui.roomsListPanel.add(temp);
            }

            currentPanel.add(tempBtn);
            i++;
        }
        if (i % 2 == 0) {
            currentPanel.add(ui.blankPanel);
        }
    }

    public void cancelKnock() {
        System.out.println("Cancel klock.");
        try {
            client.getTempRemoteDoor().cancelKnock(client.getUserId());
            tempHostIp = null;
            tempHostRegistorPort = -1;
            client.unbindAndSetNull();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void reFreshRoomsListPanel() {
        ui.roomsListPanel.removeAll();
        ui.roomsListPanel.setPreferredSize(new Dimension(0, 170));
        ui.firstPanel.removeAll();
        ui.roomsListPanel.revalidate();
        ui.roomsListPanel.repaint();
        ui.roomsListPanel.add(ui.firstPanel);
        ui.firstPanel.add(ui.btnCreateRoom);
    }

    protected void filtRoomsList() {
        reFreshRoomsListPanel();
        JPanel currentPanel = ui.firstPanel;
        int i = 0;
        for (JButton btn : ui.roomsBtnVec) {
            if (i % 2 != 0) {
                ui.roomsListPanel.setPreferredSize(new Dimension(0, (i / 2 + 2) * 170));
                JPanel temp = new JPanel();
                temp.setBounds(5, (i / 2 + 1) * 170, 570, 160);
                temp.setLayout(new GridLayout(1, 2, 5, 0));
                currentPanel = temp;
                ui.roomsListPanel.add(temp);
            }
            String[] roomInfo = btn.getText().split(" - ");
            if ((ui.roomNameTextField.getText().equals(roomInfo[0]) || ui.roomNameTextField.getText().equals(""))
                    && (ui.hostNameTextField.getText().equals(roomInfo[1])
                    || ui.hostNameTextField.getText().equals(""))) {
                currentPanel.add(btn);
                i++;
            }
        }
        if (i % 2 == 0) {
            currentPanel.add(ui.blankPanel);
        }
    }

}
