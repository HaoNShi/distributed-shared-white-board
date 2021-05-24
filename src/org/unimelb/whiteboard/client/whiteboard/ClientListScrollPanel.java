package org.unimelb.whiteboard.client.whiteboard;

import org.unimelb.whiteboard.client.user.User;
import org.unimelb.whiteboard.client.user.UserManager;

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
    private JScrollPane scrollPane;
    private JList<String> userList;
    private JButton btnAgree;
    private JButton btnKickOut;
    private JButton btnDisagree;
    private JPanel applicantControlPanel;
    private JPanel memberControlPanel;
    private String selectUserId;

    public ClientListScrollPanel(UserManager userManager) {
        this.userManager = userManager;
        initWindow();
        userManager.setCLSP(this);
        updateUserList();
    }

    /**
     * Update user list UI.
     */
    public void updateUserList() {
        Vector<String> listData = new Vector<>();
        // Add manager
        listData.add("[manager] " + userManager.getManager().getUserId());
        // Add member
        Map<String, User> members = userManager.getMembers();
        for (User x : members.values()) {
            listData.add("[member] " + x.getUserId());
        }
        // Add applicant
        if (userManager.isManager()) {
            Map<String, User> applicants = userManager.getApplicants();
            for (User x : applicants.values()) {
                listData.add("[applicant] " + x.getUserId());
                NewApplicantWindow applicantWindow = new NewApplicantWindow(this, userManager, x.getUserId());
                applicantWindow.setVisible(true);
            }
        }
        userList.setListData(listData);
    }

    private void initWindow() {
        // initialize
        setLayout(new BorderLayout());
        JPanel userListPanel = new JPanel();
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(200, 200));

        // applicant
        applicantControlPanel = new JPanel(new GridLayout(1, 2));
        btnAgree = new JButton("Agree");
        btnAgree.addActionListener(new agreeActionListener());
        applicantControlPanel.add(btnAgree);
        btnDisagree = new JButton("Disagree");
        btnDisagree.addActionListener(new disagreeActionListener());
        applicantControlPanel.add(btnDisagree);

        // member
        memberControlPanel = new JPanel(new GridLayout(1, 1));
        btnKickOut = new JButton("Kick Out");
        btnKickOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userManager.kickMember(selectUserId);
                removeBtn();
            }
        });
        memberControlPanel.add(btnKickOut);

        // The user list should be create after the control panel.
        userList = new JList<>();
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
                            if (userManager.isManager()) {
                                int identity = userManager.getIdentity(selectUserId);
                                if (identity == User.MANAGER) {
                                    removeBtn();
                                } else if (identity == User.MEMBER) {
                                    remove(applicantControlPanel);
                                    add(memberControlPanel, BorderLayout.SOUTH);
                                    revalidate();
                                    repaint();
                                } else {
                                    remove(memberControlPanel);
                                    add(applicantControlPanel, BorderLayout.SOUTH);
                                    revalidate();
                                    repaint();
                                }
                            }
                        } else {
                            removeBtn();
                        }
                    } else {
                        removeBtn();
                    }
                }
            }
        });
        userListPanel.setLayout(new BorderLayout(0, 0));

        scrollPane.add(userList);
        scrollPane.setViewportView(userList);
        userListPanel.add(scrollPane);
        add(userListPanel, BorderLayout.CENTER);
    }

    protected void removeBtn() {
        remove(memberControlPanel);
        remove(applicantControlPanel);
        revalidate();
        repaint();
    }

    public class agreeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            userManager.addMember(selectUserId);
            removeBtn();
        }
    }

    public class disagreeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            userManager.kickApplicant(selectUserId);
            removeBtn();
        }
    }
}
