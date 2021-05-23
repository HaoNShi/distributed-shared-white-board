package org.unimelb.whiteboard.client;

import com.alibaba.fastjson.JSONObject;
import org.unimelb.whiteboard.client.lobby.LobbyWindow;
import org.unimelb.whiteboard.client.login.LoginWindow;
import org.unimelb.whiteboard.client.remote.IRemoteApp;
import org.unimelb.whiteboard.client.remote.IRemoteDoor;
import org.unimelb.whiteboard.client.remote.RemoteApp;
import org.unimelb.whiteboard.client.util.Execute;
import org.unimelb.whiteboard.client.whiteboard.ClientWhiteBoard;
import org.unimelb.whiteboard.client.whiteboard.ServerWhiteBoard;
import org.unimelb.whiteboard.client.whiteboard.SharedWhiteBoard;
import org.unimelb.whiteboard.standard.StateCode;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.util.Map;

public class Client {
    public static final int TIMEOUT = 600;
    // RoomList
    public Map<Integer, String> roomList = null;
    // Windows
    private LoginWindow loginWindow = null;
    private LobbyWindow lobbyWindow = null;
    // User information
    private String ip = "127.0.0.1";
    private String userId = "";
    // Central server information
    private String serverIp = "";
    private int serverPort = -1;
    // SharedWhiteBoard
    private SharedWhiteBoard sharedWhiteBoard = null;
    // RMI
    private int registryPort;
    private Registry registry;
    // Private Remote interface for user manager.
    private IRemoteApp remoteApp;
    // before being accept
    private ClientWhiteBoard tempClientWhiteBoard = null;
    // Use to store the temporary remote door.
    private IRemoteDoor tempRemoteDoor;
    // Store the current save path.
    private String CurrentSavePath;

    public Client() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    /**
     * Run the program.
     */
    public void run() {
        loginWindow.getFrame().setVisible(true);
        System.out.println("App running");
    }

    /**
     * Get the temporary remote door.
     */
    public IRemoteDoor getTempRemoteDoor() {
        return tempRemoteDoor;
    }

    /**
     * Set the temporary remote door.
     */
    public void setTempRemoteDoor(IRemoteDoor tempRemoteDoor) {
        this.tempRemoteDoor = tempRemoteDoor;
    }

    /**
     * Get local registry
     */
    public Registry getRegistry() {
        return registry;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    /**
     * Get the Host IP address.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Get central server's ip.
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * Set server IP
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    /**
     * Get central server's port.
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Set port.
     */
    public void setServerPort(int port) {
        this.serverPort = port;
    }

    /**
     * Get userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * When user completes sign in, use this method to switch to Lobby.
     */
    public void switch2Lobby() {
        System.out.println("User: " + userId + " enter Lobby.");
        if (lobbyWindow == null)
            lobbyWindow = new LobbyWindow(this);

        lobbyWindow.setWaitDialogVisible(false);
        lobbyWindow.setBeKickedDialogVisible(false);

        loginWindow.getFrame().setVisible(false);

        unbindAndSetNull();

        lobbyWindow.getController().refreshRoomsList();
        lobbyWindow.getFrame().setVisible(true);
    }

    /**
     * Unbind um paint door, so that next time when user create a whiteboard, they
     * can be bind.
     */
    public void unbindAndSetNull() {
        try {
            registry.unbind("um");
            registry.unbind("paint");
            registry.unbind("door");
        } catch (NotBoundException e) {
            // do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sharedWhiteBoard != null) {
            sharedWhiteBoard.getWindow().getFrame().setVisible(false);
            sharedWhiteBoard = null;
        }
        tempClientWhiteBoard = null;
        tempRemoteDoor = null;
    }

    /**
     * Switch to whiteBoard.
     */
    public void openWhiteBoard() {
        lobbyWindow.setWaitDialogVisible(false);
        lobbyWindow.setBeKickedDialogVisible(false);

        lobbyWindow.getFrame().setVisible(false);
        loginWindow.getFrame().setVisible(false);
        sharedWhiteBoard.getWindow().getFrame().setVisible(true);
    }

    /**
     * Switch to Login window.
     */
    public void openLogin(Boolean isCentralServerCrush) {
        if (lobbyWindow != null) {
            lobbyWindow.getFrame().setVisible(false);
        }

        if (sharedWhiteBoard != null) {
            sharedWhiteBoard.getWindow().getFrame().setVisible(false);
        }

        loginWindow.getFrame().setVisible(true);

        if (isCentralServerCrush) {
            JOptionPane.showMessageDialog(loginWindow.getFrame(), "Can't connect to the Server. Please try again.");
        }
    }

    /**
     * Create a room and register in central server.
     */
    public void createRoom(String roomName, String password) {
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.ADD_ROOM);
        reqJSON.put("roomName", roomName);
        reqJSON.put("password", password);
        reqJSON.put("hostName", userId);
        reqJSON.put("hostIp", ip);
        reqJSON.put("hostPort", registryPort);
        JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJSON.getInteger("state");
        if (state == StateCode.SUCCESS) {
            sharedWhiteBoard = new ServerWhiteBoard(this);
            sharedWhiteBoard.setRoomId(resJSON.getInteger("roomId"));
            openWhiteBoard();
        } else {
            System.out.println("Fail to create room.");
        }
    }

    public void joinRoom(String hostIp, int chatPort) {
        tempClientWhiteBoard.createChatClient(hostIp, chatPort);
        sharedWhiteBoard = tempClientWhiteBoard;
        openWhiteBoard();
    }


    public void createTempClientWhiteBoard(String hostId, String hostIp, int hostRegisterPort) {
        tempClientWhiteBoard = new ClientWhiteBoard(this, hostId, hostIp, hostRegisterPort);
    }

    public LobbyWindow getLobbyWindow() {
        return lobbyWindow;
    }

    public void pullRemoteRoomList() {
        // sent request to central server to gain roomList
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.GET_ROOM_LIST);
        System.out.println("Request for rooms list...");
        JSONObject resJson = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJson.getIntValue("state");
        if (state == StateCode.SUCCESS) {
            roomList = (Map<Integer, String>) resJson.get("roomList");
        } else {
            System.out.println("Can't get rooms list!");
            openLogin(true);
        }
        System.out.println("Get rooms list!");
    }

    /**
     * Use to register in the central server, so no one can use a same user name.
     *
     * @return isSuccess whether the user can register in the central server or not.
     */
    public int register() {
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.ADD_USER);
        reqJSON.put("userId", userId);
        JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJSON.getIntValue("state");
        if (state == StateCode.CONNECTION_FAIL) {
            System.out.println("Connection Fail: " + state);
        } else {
            if (state == StateCode.SUCCESS) {
                System.out.println("Register in the server successfully");
            } else if (state == StateCode.FAIL) {
                System.out.println("User name exists");
            } else {
                System.out.println("Can't connect to the server.");
            }
        }
        return state;
    }

    /**
     * Delete all the information about the user in the third party. In the central
     * server, since the user remove, it would auto remove the room of the user.
     */
    public void removeUser() {
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.REMOVE_USER);
        reqJSON.put("userId", userId);
        JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJSON.getIntValue("state");
        if (state == StateCode.CONNECTION_FAIL) {
            System.out.println("Connection Fail: " + state);
        } else {
            if (state == StateCode.SUCCESS) {
                System.out.println("Exit from the server successfully");
            } else {
                System.out.println("Exit invalidly");
            }
        }
    }

    public void removeRoom() {
        int roomId = sharedWhiteBoard.getRoomId();
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("command", StateCode.REMOVE_ROOM);
        reqJSON.put("roomId", roomId);
        JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
        int state = resJSON.getIntValue("state");
        if (state == StateCode.CONNECTION_FAIL) {
            System.out.println("Connection Fail: " + state);
        } else {
            if (state == StateCode.SUCCESS) {
                System.out.println("Remove room from server successfully");
            } else {
                System.out.println("Remove room fail");
            }
        }
    }

    public String getCurrentSavePath() {
        return CurrentSavePath;
    }

    public void setCurrentSavePath(String path) {
        this.CurrentSavePath = path;
    }

    private void init() {
        initRMI();
        loginWindow = new LoginWindow(this);
    }

    private void initRMI() {
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.tcp.responseTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.tcp.readTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.connectionTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.proxy.connectTimeout", String.valueOf(TIMEOUT));
            System.setProperty("sun.rmi.transport.tcp.handshakeTimeout", String.valueOf(TIMEOUT));

            RMISocketFactory.setSocketFactory(new RMISocketFactory() {
                public Socket createSocket(String host, int port) throws IOException {
                    Socket socket = new Socket();
                    socket.setSoTimeout(TIMEOUT);
                    socket.setSoLinger(false, 0);
                    socket.connect(new InetSocketAddress(host, port), TIMEOUT);
                    return socket;
                }

                public ServerSocket createServerSocket(int port) throws IOException {
                    return new ServerSocket(port);
                }
            });

            // Get a random port (Available one).
            ServerSocket registrySocket = new ServerSocket(0);
            registryPort = registrySocket.getLocalPort();
            registrySocket.close();

            // Start RMI registry
            LocateRegistry.createRegistry(registryPort);
            registry = LocateRegistry.getRegistry(ip, registryPort);

            // Create a remote user manager
            remoteApp = new RemoteApp(this);
            registry.bind("app", remoteApp);

            printInitialStates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printInitialStates() {
        System.out.println("IP address : " + ip);
        System.out.println("Registry Port = " + registryPort);
    }

}
