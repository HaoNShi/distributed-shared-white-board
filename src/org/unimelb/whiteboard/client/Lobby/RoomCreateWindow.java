package org.unimelb.whiteboard.client.Lobby;

import org.unimelb.whiteboard.client.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class RoomCreateWindow extends JDialog {
	private static final long serialVersionUID = 1953383401687653792L;
	private final JPanel contentPanel = new JPanel();
	private final JTextField roomNameTextField;
	private final JTextField PasswordTextField;
	private final Client client;
	private final JLabel lblError;
	private String roomName = "";
	private String password = "";

	/**
	 * Create the dialog.
	 */
	public RoomCreateWindow(Client client) {
		this.client = client;
		setTitle("Create Room");
		setResizable(false);
		setBounds(100, 100, 384, 239);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblRoomName = new JLabel("Room Name:");
		lblRoomName.setBounds(49, 24, 106, 27);
		lblRoomName.setFont(new Font("Arial", Font.PLAIN, 15));
		contentPanel.add(lblRoomName);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(49, 78, 106, 27);
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 15));
		contentPanel.add(lblPassword);

		roomNameTextField = new JTextField();
		roomNameTextField.setBounds(161, 24, 150, 27);
		roomNameTextField.setFont(new Font("Arial", Font.PLAIN, 15));
		roomNameTextField.setColumns(10);
		contentPanel.add(roomNameTextField);

		PasswordTextField = new JTextField();
		PasswordTextField.setBounds(161, 78, 150, 27);
		PasswordTextField.setFont(new Font("Arial", Font.PLAIN, 15));
		PasswordTextField.setColumns(10);
		contentPanel.add(PasswordTextField);

		lblError = new JLabel("Error");
		lblError.setFont(new Font("Arial", Font.PLAIN, 12));
		lblError.setBounds(49, 115, 262, 16);
		lblError.setForeground(Color.RED);
		lblError.setVisible(false);

		contentPanel.add(lblError);

		JButton okButton = new JButton("OK");
		okButton.setFont(new Font("Arial", Font.PLAIN, 12));
		okButton.setBounds(49, 141, 106, 36);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				roomName = roomNameTextField.getText();
				password = PasswordTextField.getText();
				if (roomInfoCheck()) {
					client.createRoom(roomName, password);
					dispose();
				}
			}
		});
		okButton.setActionCommand("OK");
		contentPanel.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Arial", Font.PLAIN, 12));
		cancelButton.setBounds(205, 141, 106, 36);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		contentPanel.add(cancelButton);
	}

	public static void showCreateRoomDialog(Frame owner, Component parentComponent, Client client) {
		final RoomCreateWindow roomCreateWindow = new RoomCreateWindow(client);
		roomCreateWindow.setLocationRelativeTo(parentComponent);
		roomCreateWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		roomCreateWindow.setModal(true);
		roomCreateWindow.setVisible(true);
	}

	private Boolean roomInfoCheck() {
		String roomNamePatten = "^\\w{1,8}$";
		String passwordPatten = "^\\w{0,8}$";
		String err;

		if (!Pattern.matches(roomNamePatten, roomName)) {
			err = "Room Name Format: \\w{1,8}";
			lblError.setText(err);
			lblError.setVisible(true);
			return false;
		}
		if (!Pattern.matches(passwordPatten, password)) {
			err = "Password Format: \\w{,8}";
			lblError.setText(err);
			lblError.setVisible(true);
			return false;
		}
		return true;
	}

	public Client getClient() {
		return client;
	}
}
