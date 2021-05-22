package org.unimelb.whiteboard.server;

import org.unimelb.whiteboard.server.request.RequestHandler;
import org.unimelb.whiteboard.server.room.RoomManager;
import org.unimelb.whiteboard.server.ui.ServerWindow;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Central Server, act as a authentic third party to provide information about
 * users and White Board room.
 * <p>
 * The Central Server's default running port is DEFAULT_PORT
 */

public class Server {
    public static final int DEFAULT_PORT = 8080;
    private final RoomManager rm;
    private int port;
    private ServerSocket server;
    private ServerWindow ui;
    private String ip = "127.0.0.1";
    private Map<String, String> userList;

    /**
     * Default port is DEFAULT_PORT
     */
    public Server() throws IOException {
        this.port = DEFAULT_PORT;
        rm = new RoomManager();
        init();
    }

    /**
     * Create with a specific port.
     */
    public Server(int port) throws IOException {
        this.port = port;
        rm = new RoomManager();
        init();
    }

    public static void main(String[] args) {
        try {
            Server server = null;
            if (args.length == 1) {
                if (Integer.parseInt(args[0]) <= 1024 || Integer.parseInt(args[0]) >= 49151) {
                    System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
                    System.exit(-1);
                } else {
                    server = new Server(Integer.parseInt(args[0]));
                }
            } else {
                server = new Server();
            }
            server.run();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Lack of Parameters:\nPlease run like \"java - jar CentralServer.jar <port>\"!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
        } catch (BindException e) {
            System.err.println("Address already in use!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the Program.
     */
    public void run() throws IOException {
        ui.getFrame().setVisible(true);
        while (true) {
            Socket client = server.accept();
            RequestHandler rh = new RequestHandler(client, this);
            rh.start();
        }
    }

    /**
     * Set the port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Print text on both command line and GUI.
     */
    public void printOnBoth(String str) {
        System.out.println(str);
        if (ui != null)
            ui.getLogArea().append(str + '\n');
    }

    /**
     * Get the room manager.
     */
    public RoomManager getRoomManager() {
        return rm;
    }

    /**
     * Get the user list.
     */
    public Map<String, String> getUserList() {
        return userList;
    }

    private void init() throws IOException {
        // Get IP address of Localhost
        server = new ServerSocket(this.port);
        // Use to record the user add into the server.
        userList = new HashMap<>();
        printInitialStats();
        this.ui = new ServerWindow(ip, String.valueOf(port));
    }

    private void printInitialStats() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("Server Running...");
        System.out.println("Current IP address : " + ip.getHostAddress());
        System.out.println("Port = " + port);
        System.out.println("Waiting for client connection...\n--------------");
    }
}
