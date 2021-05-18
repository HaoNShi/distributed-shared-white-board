package org.unimelb.whiteboard.client.WhiteBoard;

import org.unimelb.whiteboard.client.ClientUser.User;
import org.unimelb.whiteboard.client.ClientUser.UserManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

public class ClientListScrollPanel extends JPanel {
    private final UserManager userManager;
    private JScrollPane scrollpane;
    private JList<String> userList;
    private JButton btnAgree;
    private JButton btnKickOut;
    private JButton btnDisagree;
    private JPanel visitorControlPanel;
    private JPanel guestControlPanel;
    private String selectUserId;

    public ClientListScrollPanel(UserManager userManager) {
        this.userManager = userManager;
        initView();
        userManager.setCLSP(this);
        updateUserList();
    }

    /**
     * Update user list UI.
     */
    public void updateUserList() {
        Vector<String> listData = new Vector<String>();
        // 1. add host.
        listData.add("[host] " + userManager.getHost().getUserId());
        // 2. add guest.
        Map<String, User> guests = userManager.getGuests();
        for (User x : guests.values()) {
            listData.add("[guest] " + x.getUserId());
        }

        if (userManager.isHost()) {
            Map<String, User> visitors = userManager.getVisitors();
            for (User x : visitors.values()) {
                listData.add("[visitor] " + x.getUserId());
            }
        }
        userList.setListData(listData);
    }

    private void initView() {
        // initialize
        setLayout(new BorderLayout());
        JPanel userListPanel = new JPanel();
        scrollpane = new JScrollPane();
        scrollpane.setPreferredSize(new Dimension(200, 200));

        // visitor
        visitorControlPanel = new JPanel(new GridLayout(1, 2));
        btnAgree = new JButton("Agree");
        btnAgree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userManager.addGuest(selectUserId);
                removeBtns();
            }
        });
        visitorControlPanel.add(btnAgree);
        btnDisagree = new JButton("Disagree");
        btnDisagree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userManager.kickVisitor(selectUserId);
                removeBtns();
            }
        });
        visitorControlPanel.add(btnDisagree);

        // guest
        guestControlPanel = new JPanel(new GridLayout(1, 1));
        btnKickOut = new JButton("Kick Out");
        btnKickOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userManager.kickGuest(selectUserId);
                removeBtns();
            }
        });
        guestControlPanel.add(btnKickOut);

        // The user list should be create after the control panel.
        userList = new JList<String>();
        userList.setForeground(Color.black);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // select client(s)
        userList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!userList.getValueIsAdjusting()) {
                    String select = userList.getSelectedValue();
                    if (select != null) {
                        String[] tempStrings = userList.getSelectedValue().split("] ");
                        if (tempStrings[1] != null) {
                            selectUserId = tempStrings[1];
                            if (userManager.isHost()) {
                                int identity = userManager.getIdentity(selectUserId);
                                if (identity == User.HOST) {
                                    removeBtns();
                                } else if (identity == User.GUEST) {
                                    remove(visitorControlPanel);
                                    add(guestControlPanel, BorderLayout.SOUTH);
                                    revalidate();
                                    repaint();
                                } else {
                                    remove(guestControlPanel);
                                    add(visitorControlPanel, BorderLayout.SOUTH);
                                    revalidate();
                                    repaint();
                                }
                            }
                        } else {
                            removeBtns();
                        }
                    } else {
                        removeBtns();
                    }
                }
            }
        });
        userListPanel.setLayout(new BorderLayout(0, 0));

        scrollpane.add(userList);
        scrollpane.setViewportView(userList);
        userListPanel.add(scrollpane);
        add(userListPanel, BorderLayout.CENTER);
    }

    private void removeBtns() {
        remove(guestControlPanel);
        remove(visitorControlPanel);
        revalidate();
        repaint();
    }
}
