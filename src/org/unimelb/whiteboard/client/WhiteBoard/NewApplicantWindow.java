package org.unimelb.whiteboard.client.WhiteBoard;

import org.unimelb.whiteboard.client.User.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewApplicantWindow extends JDialog {
    private static final long serialVersionUID = -1582604536132283889L;
    private final JPanel contentPanel = new JPanel();
    private final JButton agreeBtn;
    private final JButton disagreeBtn;
    private final JButton ignoreBtn;
    private int count = 10;

    /**
     * Timer
     */
    Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            count--;
            if (count < 0) {
                timer.stop();
                NewApplicantWindow.this.dispose(); // 时间倒数完毕，销毁Dialog
            } else
                ignoreBtn.setText("Ignore(" + count + ")");
        }
    });

    /**
     * Create the dialog.
     */
    public NewApplicantWindow(ClientListScrollPanel clsp, UserManager userManager, String userId) {
        setBounds(100, 100, 390, 180);
        setTitle("New Applicant");
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblTip = new JLabel("User " + userId + " applies to enter the room.");
        lblTip.setBounds(33, 33, 300, 15);
        contentPanel.add(lblTip);

        agreeBtn = new JButton("Agree");
        agreeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userManager.addMember(userId);
                clsp.removeBtn();
                NewApplicantWindow.this.dispose();
            }
        });
        agreeBtn.setBounds(20, 73, 100, 30);
        contentPanel.add(agreeBtn);

        disagreeBtn = new JButton("Disagree");
        disagreeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userManager.kickApplicant(userId);
                clsp.removeBtn();
                NewApplicantWindow.this.dispose();
            }
        });
        disagreeBtn.setBounds(134, 73, 100, 30);
        contentPanel.add(disagreeBtn);

        ignoreBtn = new JButton("Ignore(" + count + ")");
        ignoreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewApplicantWindow.this.dispose();
            }
        });
        ignoreBtn.setBounds(247, 73, 100, 30);
        contentPanel.add(ignoreBtn);

        timer.start();
    }

}
