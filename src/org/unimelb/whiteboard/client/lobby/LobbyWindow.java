package org.unimelb.whiteboard.client.lobby;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.util.LobbyCloseListener;
import org.unimelb.whiteboard.client.util.WaitDialogCloseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class LobbyWindow {

    protected JFrame frame;

    protected JButton btnCreateRoom;
    protected JPanel firstPanel;
    protected JPanel blankPanel;
    protected JScrollPane scrollPane;
    protected Vector<JButton> roomsBtnVec;
    protected Client client;
    protected LobbyController controller;

    protected String addImagePath = "images/add.png";
    protected String joinImagePath = "images/join.png";

    protected JOptionPane waitPane;
    protected JDialog waitDialog;

    protected JOptionPane beKickedPane;
    protected JDialog beKickedDialog;

    /**
     * Create the application.
     */
    public LobbyWindow(Client client) {
        this.client = client;
        roomsBtnVec = new Vector<>();
        initialize();
        controller = new LobbyController(client, this);
    }

    public LobbyController getController() {
        return controller;
    }

    /**
     * Get current frame.
     */
    public JFrame getFrame() {
        return this.frame;
    }

    /**
     * Set waitDialog visible.
     */
    public void setWaitDialogVisible(Boolean isVisible) {
        if (waitDialog != null)
            waitDialog.setVisible(isVisible);
    }

    /**
     * Set beKickedDialog visible.
     */
    public void setBeKickedDialogVisible(Boolean isVisible) {
        if (beKickedDialog != null)
            beKickedDialog.setVisible(isVisible);
    }

    /**
     * Create a wait dialog, not visible.
     */
    public void createWaitDialog() {
        waitDialog = waitPane.createDialog(frame, "Waiting");
        waitDialog.addWindowListener(new WaitDialogCloseListener(controller));
    }

    /**
     * Create a beKicked dialog, visible.
     */
    public void createBeKickedDialog() {
        beKickedDialog = beKickedPane.createDialog(frame, "Be Kicked");
        beKickedDialog.setModal(false);
        beKickedDialog.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Lobby");
        frame.setBounds(100, 100, 600, 419);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new LobbyCloseListener(client));
        frame.setMinimumSize(new Dimension(600, 400));
        frame.getContentPane().setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 596, 472);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(scrollPane);

        // FirstPanel In RoomList Panel INIT
        firstPanel = new JPanel();
        firstPanel.setBounds(5, 5, 570, 160);
        firstPanel.setLayout(new GridLayout(1, 2, 5, 0));

        // CREATE_ROOM button INIT
        btnCreateRoom = new JButton();
        btnCreateRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RoomCreateWindow.showCreateRoomDialog(frame, frame, client);
            }
        });
        ImageIcon addIcon = new ImageIcon(addImagePath);
        addIcon.setImage(addIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        btnCreateRoom.setIcon(addIcon);

        // Initialize Blank Panel
        blankPanel = new JPanel();

        // Use to cancel knock.
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                waitDialog.setVisible(false);
                controller.cancelKnock();
            }
        });
        JButton[] cancelBtnOption = {cancelBtn};
        waitPane = new JOptionPane("Waiting for permission...", JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, cancelBtnOption, cancelBtnOption[0]);

        // Use to warn be kicked.
        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                beKickedDialog.setVisible(false);
            }
        });
        JButton[] okBtnOption = {okBtn};
        beKickedPane = new JOptionPane("You have been kicked out.", JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, null, okBtnOption, okBtnOption[0]);
    }
}
