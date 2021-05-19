package org.unimelb.whiteboard.client.Lobby;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.util.LobbyCloseListener;
import org.unimelb.whiteboard.util.WaitDialogCloseListener;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class LobbyWindow {

    protected JFrame frame;
    protected JTextField roomNameTextField;
    protected JTextField hostNameTextField;

    protected JPanel roomsListPanel;
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
        roomsBtnVec = new Vector<JButton>();
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
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new LobbyCloseListener(client));
        frame.setMinimumSize(new Dimension(600, 500));
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        roomsListPanel = new JPanel();
        // panel.setPreferredSize(new Dimension(0, 500));
        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(roomsListPanel);
        roomsListPanel.setLayout(null);

        // FirstPanel In RoomList Panel INIT
        firstPanel = new JPanel();
        firstPanel.setBounds(5, 5, 570, 160);
        firstPanel.setLayout(new GridLayout(1, 2, 5, 0));

        // CREATE_ROOM button INIT
        btnCreateRoom = new JButton();
        btnCreateRoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RoomCreateDialog.showCreateRoomDialog(frame, frame, client);
            }
        });
        ImageIcon addIcon = new ImageIcon(addImagePath);
        addIcon.setImage(addIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        btnCreateRoom.setIcon(addIcon);

        // init Blank Panel
        blankPanel = new JPanel();

        JPanel controlBarPanel = new JPanel();
        controlBarPanel.setPreferredSize(new Dimension(0, 100));
        frame.getContentPane().add(controlBarPanel, BorderLayout.SOUTH);
        controlBarPanel.setLayout(new BorderLayout(0, 0));

        JButton btnRefresh = new JButton("REFRESH");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.refreshRoomsList();
            }
        });
        controlBarPanel.add(btnRefresh, BorderLayout.EAST);

        // filterPanel
        JPanel filterPanel = new JPanel();
        controlBarPanel.add(filterPanel, BorderLayout.CENTER);
        filterPanel.setLayout(new BorderLayout(0, 0));

        JButton btnFilt = new JButton("FILT");
        btnFilt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.filtRoomsList();
            }
        });

        filterPanel.add(btnFilt, BorderLayout.EAST);
        JPanel filterTextPanel = new JPanel();
        filterPanel.add(filterTextPanel, BorderLayout.CENTER);
        JLabel lblRoomName = new JLabel("Room Name:");
        JLabel lblHostName = new JLabel("Host Name:");

        roomNameTextField = new JTextField();
        roomNameTextField.setColumns(10);

        hostNameTextField = new JTextField();
        hostNameTextField.setColumns(10);
        GroupLayout gl_filterTextPanel = new GroupLayout(filterTextPanel);
        gl_filterTextPanel.setHorizontalGroup(gl_filterTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_filterTextPanel.createSequentialGroup().addGap(50).addGroup(gl_filterTextPanel
                        .createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_filterTextPanel.createSequentialGroup()
                                .addComponent(lblRoomName, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                .addGap(13).addComponent(roomNameTextField))
                        .addGroup(gl_filterTextPanel.createSequentialGroup().addGap(7)
                                .addComponent(lblHostName, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED).addComponent(hostNameTextField)))
                        .addGap(50)));
        gl_filterTextPanel.setVerticalGroup(gl_filterTextPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_filterTextPanel.createSequentialGroup().addGap(23)
                        .addGroup(gl_filterTextPanel.createParallelGroup(Alignment.LEADING)
                                .addGroup(
                                        gl_filterTextPanel.createSequentialGroup().addGap(5).addComponent(lblRoomName))
                                .addComponent(roomNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGap(2)
                        .addGroup(gl_filterTextPanel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(hostNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblHostName))
                        .addContainerGap(23, Short.MAX_VALUE)));
        filterTextPanel.setLayout(gl_filterTextPanel);

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
