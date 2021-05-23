package org.unimelb.whiteboard.client.whiteboard;

import org.unimelb.whiteboard.client.Client;
import org.unimelb.whiteboard.client.chatroom.ChatPanel;
import org.unimelb.whiteboard.client.menu.EditMenu;
import org.unimelb.whiteboard.client.menu.FileMenu;
import org.unimelb.whiteboard.client.user.UserManager;
import org.unimelb.whiteboard.client.util.NumberTextField;
import org.unimelb.whiteboard.client.util.WhiteBoardCloseListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * When you need to using the WindowBuilder to design the whiteBoardWindow, annotate the code in PaintBoardPanel.java.
 */
public class WhiteBoardWindow {
    // Default color display in the left bottom.
    private static final Color[] COLORS = {Color.BLACK, Color.GRAY, new Color(136, 0, 21), Color.RED, new Color(255, 127, 39), Color.YELLOW, new Color(34, 177, 76), Color.BLUE,
            Color.WHITE, Color.LIGHT_GRAY, new Color(185, 122, 87), Color.PINK, Color.ORANGE, new Color(239, 228, 176), Color.GREEN, new Color(63, 72, 204)};
    // Use to create tool button.
    private static final String[] TOOLS = {"pen", "line", "circle", "oval",
            "eraser", "rect", "roundrect", "text"};
    private final JColorChooser colorChooser;
    // Title of the window
    private final String title;
    // Paint history recorder
    private final PaintManager paintManager;
    // User Manager
    private final UserManager userManager;
    // Client
    private final Client client;
    private final Color backgroundColor;
    private JFrame frame;
    private DrawListener drawListener;
    private PaintBoardPanel paintBoardPanel;
    private JPanel chatRoomControlPanel;
    private JButton btnCurrentColor;
    private JButton btnEditColor;
    // Color
    private Color currentColor;
    // Thickness
    private int thickness;
    private JTextField thicknessTextField;

    /**
     * Create the window with Paint Manager.
     */
    public WhiteBoardWindow(Client client, PaintManager paintManager, UserManager userManager, String title) {
        this.title = title;
        this.client = client;
        currentColor = Color.BLACK;
        thickness = 2;
        backgroundColor = Color.WHITE;
        colorChooser = new JColorChooser(currentColor);
        this.paintManager = paintManager;
        this.userManager = userManager;
        initialize();
    }

    public void setChatPanel(ChatPanel chatPanel) {
        chatRoomControlPanel.add(chatPanel, BorderLayout.CENTER);
    }

    /**
     * Get frame window.
     */
    public JFrame getFrame() {
        return this.frame;
    }

    /**
     * Get getPaintBoardFrame window.
     */
    public PaintBoardPanel getPaintBoardPanel() {
        return this.paintBoardPanel;
    }

    /**
     * Get current select color.
     */
    public Color getCurrentColor() {
        return currentColor;
    }

    /**
     * Get thickness.
     */
    public int getThickness() {
        return thickness;
    }

    /**
     * Get background color.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Get paint history manager
     */
    public PaintManager getPaintManager() {
        return paintManager;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // When close the window, it should remove its information in the system.
        frame.addWindowListener(new WhiteBoardCloseListener(client, paintManager, userManager));
        frame.setSize(1105, 800);
        frame.setTitle(title);
        frame.setResizable(true);

        // Add Action Listener
        drawListener = new DrawListener(this);

        // Add Painting broad.
        paintBoardPanel = new PaintBoardPanel(paintManager);
        paintBoardPanel.setBackground(Color.white);
        paintBoardPanel.addMouseListener(drawListener);
        paintBoardPanel.addMouseMotionListener(drawListener);
        paintBoardPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        frame.getContentPane().add(paintBoardPanel, BorderLayout.CENTER);

        // Set the paint area in paintManager to the current paintBoard panel.
        paintManager.setPaintArea(paintBoardPanel);

        JPanel userPanel = new JPanel();
        userPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        userPanel.setPreferredSize(new Dimension(200, 0));
        userPanel.setBackground(Color.WHITE);
        frame.getContentPane().add(userPanel, BorderLayout.EAST);
        userPanel.setLayout(new BorderLayout(0, 0));

        JPanel userControlPanel = new JPanel();
        userControlPanel.setPreferredSize(new Dimension(0, 200));
        userPanel.add(userControlPanel, BorderLayout.NORTH);
        userControlPanel.setLayout(new BorderLayout(0, 0));

        JLabel lblUserList = new JLabel("User List");
        userControlPanel.add(lblUserList, BorderLayout.NORTH);

        ClientListScrollPanel clientListScrollPanel = new ClientListScrollPanel(userManager);
        userControlPanel.add(clientListScrollPanel, BorderLayout.CENTER);

        chatRoomControlPanel = new JPanel();
        userPanel.add(chatRoomControlPanel, BorderLayout.CENTER);
        chatRoomControlPanel.setLayout(new BorderLayout(0, 0));

        JLabel lblChatRoom = new JLabel("Chat Room");
        chatRoomControlPanel.add(lblChatRoom, BorderLayout.NORTH);

        // Create shape panel
        JPanel drawToolPanel = new JPanel();
        frame.getContentPane().add(drawToolPanel, BorderLayout.NORTH);
        drawToolPanel.setLayout(new BorderLayout(0, 0));

        JPanel toolPanel = new JPanel();
        toolPanel.setBorder(new TitledBorder(null, "Shape", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        toolPanel.setLayout(new GridLayout(2, 0, 5, 5));
        drawToolPanel.add(toolPanel, BorderLayout.WEST);

        // Add shape buttons
        JButton btnTool;
        for (String s : TOOLS) {
            btnTool = new JButton(s);
            btnTool.setCursor(new Cursor(Cursor.HAND_CURSOR));
            ImageIcon toolIcon = new ImageIcon("images/" + s + ".png");
            toolIcon.setImage(toolIcon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
            btnTool.setIcon(toolIcon);
            toolPanel.add(btnTool);
            btnTool.addActionListener(drawListener);
        }

        // Create color panel
        JPanel colorPanel = new JPanel();
        colorPanel.setBorder(new TitledBorder(null, "Color", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        drawToolPanel.add(colorPanel, BorderLayout.CENTER);
        colorPanel.setLayout(new BorderLayout(0, 0));

        // Create a new panel on the west side so that the shape will not be stretched.
        JPanel fixColorPanel = new JPanel();
        colorPanel.add(fixColorPanel, BorderLayout.WEST);

        // Current color
        JPanel currentColorPanel = new JPanel();
        fixColorPanel.add(currentColorPanel, BorderLayout.WEST);
        currentColorPanel.setLayout(new BorderLayout());

        JLabel lblCurrentColor = new JLabel("   Current");
        currentColorPanel.add(lblCurrentColor, BorderLayout.SOUTH);

        JPanel currentColorSquarePanel = new JPanel();
        currentColorSquarePanel.setPreferredSize(new Dimension(60, 60));
        currentColorPanel.add(currentColorSquarePanel, BorderLayout.CENTER);

        btnCurrentColor = new JButton();
        currentColorSquarePanel.add(btnCurrentColor);
        btnCurrentColor.setBorderPainted(false);
        btnCurrentColor.setEnabled(false);

        btnCurrentColor.setBackground(currentColor);
        btnCurrentColor.setOpaque(true);
        btnCurrentColor.setPreferredSize(new Dimension(50, 50));

        // List colors
        JPanel listColorPanel = new JPanel();
        fixColorPanel.add(listColorPanel, BorderLayout.CENTER);
        listColorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        listColorPanel.setLayout(new GridLayout(2, 0, 5, 5));

        // Add item colors
        JButton btnItemColor;
        for (Color defaultColor : COLORS) {
            btnItemColor = new JButton();
            btnItemColor.setBorderPainted(false);
            btnItemColor.setBackground(defaultColor);
            btnItemColor.setOpaque(true);
            btnItemColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnItemColor.setPreferredSize(new Dimension(40, 40));
            btnItemColor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currentColor = ((JButton) e.getSource()).getBackground();
                    btnCurrentColor.setBackground(currentColor);
                }
            });
            listColorPanel.add(btnItemColor);
        }

        // Edit color
        JPanel editColorPanel = new JPanel();
        fixColorPanel.add(editColorPanel, BorderLayout.EAST);
        editColorPanel.setLayout(new BorderLayout());

        JLabel lblEditColor = new JLabel("      Edit");
        editColorPanel.add(lblEditColor, BorderLayout.SOUTH);

        JPanel editColorSquarePanel = new JPanel();
        editColorSquarePanel.setPreferredSize(new Dimension(60, 60));
        editColorPanel.add(editColorSquarePanel, BorderLayout.CENTER);

        btnEditColor = new JButton();
        editColorSquarePanel.add(btnEditColor);
        btnEditColor.setBorderPainted(false);
        btnEditColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentColor = JColorChooser.showDialog(frame, "Color Chooser", currentColor);
                btnCurrentColor.setBackground(currentColor);
            }
        });

        ImageIcon editIcon = new ImageIcon("images/palette.jpg");
        editIcon.setImage(editIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        btnEditColor.setIcon(editIcon);
        btnEditColor.setOpaque(true);
        btnEditColor.setPreferredSize(new Dimension(50, 50));

        // Create thickness panel
        JPanel thicknessPanel = new JPanel();
        thicknessPanel.setBorder(new TitledBorder(null, "Thickness", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        thicknessPanel.setPreferredSize(new Dimension(80, 0));
        drawToolPanel.add(thicknessPanel, BorderLayout.EAST);
        thicknessPanel.setLayout(new BorderLayout(0, 0));

        JPanel thicknessTextPanel = new JPanel();
        thicknessPanel.add(thicknessTextPanel, BorderLayout.CENTER);
        thicknessTextPanel.setLayout(new BorderLayout(0, 0));

        thicknessTextField = new JTextField();
        thicknessTextField.setDocument(new NumberTextField(2, true));
        thicknessTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                thickness = Integer.parseInt(thicknessTextField.getText());
            }
        });
        thicknessTextField.setText(Integer.toString(thickness));
        thicknessTextField.setHorizontalAlignment(SwingConstants.CENTER);
        thicknessTextPanel.add(thicknessTextField);
        thicknessTextField.setColumns(2);

        // Add menu bar at the last, need to wait for creation of paintBoardPanel.
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        menuBar.add(new FileMenu(client, this, paintManager, userManager));
        EditMenu editMenu = new EditMenu(this);
        menuBar.add(editMenu);

        // Add the editMenu into paint manager, so it can be controlled.
        paintManager.setEditMenu(editMenu);

        frame.setVisible(false);
    }
}
